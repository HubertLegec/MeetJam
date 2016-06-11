package com.pik.event.acceptance

import com.pik.base.MvcIntegrationSpec
import com.pik.event.EventRepository
import com.pik.event.exception.EventsError
import com.pik.event.MusicEvent
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Shared
import java.time.LocalDateTime

import static java.time.format.DateTimeFormatter.*
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class CreateAndRemoveEventSpec extends MvcIntegrationSpec {
    private static final String CREATE_EVENT_URL = '/api/event/create'
    private static final String DELETE_EVENT_URL = '/api/event/delete'
    @Shared
    private MusicEvent sampleEvent = new MusicEvent(LocalDateTime.of(2016, 4, 26, 14, 30),
            'Warsaw',
            'Sample Event',
            'Zosia')
    @Shared
    private MusicEvent someoneElseEvent = new MusicEvent(LocalDateTime.of(2016, 4, 26, 14, 30),
            'Warsaw',
            'Another Event',
            'Adam')
    @Shared
    private String token

    @Autowired
    private EventRepository eventRepository

    def setup() {
        def response = sendLoginRequest(
                testAccounts[1].getUsername(),
                testAccounts[1].getPassword())
        token = extractTokenFromResponse(response.andReturn().response.contentAsString)
    }

    def cleanup() {
        eventRepository.deleteAll()
    }

    def 'when user create new event he should receive http status created'() {
        given: 'logged-in user and event details'
        token != null
        String city = 'Warsaw'
        String title = 'Test event'
        LocalDateTime dateTime = LocalDateTime.of(2016, 04, 29, 16, 0)
        when: 'user create event'
        ResultActions response = createEvent(title, city, dateTime)
        then: 'event id is returned and event is present in database'
        response.andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
        responseContainsEventId(response)
        eventInRepository(city, title, dateTime)
    }

    def 'when user create new event with incorrect params Http NOT ACCEPTABLE status and error list should be returned '() {
        given: 'logged-in user and incorrect event details'
        token != null
        String city = 'Warsaw'
        String title = ''
        LocalDateTime dateTime = LocalDateTime.of(2016, 04, 29, 16, 0)
        when: 'user create event'
        ResultActions response = createEvent(title, city, dateTime)
        then: 'returned event id is null and error list contains messages'
        response.andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON))
        createResponseContainsError(response, EventsError.EMPTY_TITLE_FILED.message)
    }

    def 'after sending correct request for event removal, user should receive http status ok'() {
        given: 'logged-in user and event in database'
        token != null
        String id = insertSampleEvent(sampleEvent)
        when: 'user removes event'
        ResultActions response = removeEvent(id)
        then: 'response status ok and event does not present in database'
        response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
        eventRepository.findById(id) == null
    }

    def 'removal of event user does not own should be forbidden'() {
        given: 'logged-in user and event created by somebody else in database'
        token != null
        String id = insertSampleEvent(someoneElseEvent)
        when: 'user removes not her event'
        ResultActions response = removeEvent(id)
        then: 'action should fail and event should be present in database'
        response.andExpect(status().isForbidden())
                .andExpect(content().contentType(APPLICATION_JSON))
        removeResponseContainsError(response, EventsError.NOT_USERS_EVENT.message)
    }

    private ResultActions createEvent(String title, String city, LocalDateTime date) {
        ResultActions resultActions = mockMvc.perform(post(CREATE_EVENT_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('title', title)
                .param('city', city)
                .param('date', date.format(ISO_DATE_TIME)))
        return resultActions
    }

    private ResultActions removeEvent(String id) {
        ResultActions resultActions = mockMvc.perform(delete(DELETE_EVENT_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('id', id))
        return resultActions
    }

    private static boolean responseContainsEventId(ResultActions response) {
        def jsonResult = new JsonSlurper()
                .parseText(response
                .andReturn()
                .response
                .contentAsString)
        return jsonResult.id != null && jsonResult.id.length() > 0
    }

    private static boolean createResponseContainsError(ResultActions response, String error) {
        def jsonResult = new JsonSlurper()
                .parseText(response
                .andReturn()
                .response
                .contentAsString)
        return jsonResult.id == null && jsonResult.messages.contains(error)
    }

    private static boolean removeResponseContainsError(ResultActions response, String error) {
        def jsonResult = response
                .andReturn()
                .response
                .contentAsString
        return error.equals(jsonResult)
    }

    private boolean eventInRepository(String city, String title, LocalDateTime dateTime) {
        return eventRepository.findByOwner(testAccounts[1].getUsername()).findAll {
            it.city == city &&
                    it.title == title &&
                    it.date == dateTime
        }.size() == 1
    }

    private String insertSampleEvent(MusicEvent event) {
        MusicEvent returnedEvent = eventRepository.save(event)
        return returnedEvent.getId()
    }
}

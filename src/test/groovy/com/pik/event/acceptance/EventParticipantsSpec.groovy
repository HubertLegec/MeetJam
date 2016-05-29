package com.pik.event.acceptance

import com.pik.base.MvcIntegrationSpec
import com.pik.event.EventRepository
import com.pik.event.MusicEvent
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Shared
import java.time.LocalDateTime

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class EventParticipantsSpec extends MvcIntegrationSpec {
    private static final MusicEvent EVENT = new MusicEvent(LocalDateTime.of(2016, 2, 10, 0, 0), 'Warsaw', 'Jam', 'Zosia')
    private static final MusicEvent EVENT2 = new MusicEvent(LocalDateTime.of(2016, 4, 20, 0, 0), 'Warsaw', 'Jam2', 'Adam')
    private static final String PENDING_USERS_LIST_URL = '/api/event/pendingUsers'
    private static final String JOIN_EVENT_URL = '/api/event/join'
    private static final String LEAVE_EVENT_URL = '/api/event/leave'
    private static final String ACCEPT_USER_URL = '/api/event/acceptUser'
    private static final String REJECT_USER_URL = '/api/event/rejectUser'

    static {
        EVENT.addParticipant('Damian')
    }

    @Shared private String username = testAccounts[1].getUsername()
    @Shared private String token
    @Shared private String eventID
    @Autowired private EventRepository eventRepository

    def setup() {
        MusicEvent savedEvent = eventRepository.save(EVENT)
        eventID = savedEvent.getId()
        def response = sendLoginRequest(
                testAccounts[1].getUsername(),
                testAccounts[1].getPassword())
        token = extractTokenFromResponse(response.andReturn().response.contentAsString)
    }

    def cleanup() {
        eventRepository.deleteAll()
    }

    def 'user receives list of logins after sending request with id of her event'() {
        given: 'event with pending participants in database'
            List<String> participants = ['Tom', 'Eva']
            MusicEvent event = eventRepository.findById(eventID)
            participants.each { participant -> event.addParticipant(participant) }
            eventRepository.save(event)
        when: 'user sends request for pending users list'
            def response = sendGetRequest(PENDING_USERS_LIST_URL, eventID)
        then:
            response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            responseListContainsUsers(response, participants)
    }

    def 'event in database contains user on pending list after join request'() {
        given: 'Sbs event in database'
            String id = eventRepository.save(EVENT2).getId()
        when: 'user wants to join event'
            def response = sendGetRequest(JOIN_EVENT_URL, id)
        then: 'event in database contains pending user'
            response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            MusicEvent event = eventRepository.findById(id)
            event.getPendingParticipants().contains(username)
    }

    def 'joined- and pending- users list does not contains username after leave request'(){
        given: 'event user joined'
            MusicEvent event = eventRepository.save(EVENT2)
            event.addParticipant(username)
            String id = eventRepository.save(event).getId()
        when: 'user sends leave request'
            def response = sendGetRequest(LEAVE_EVENT_URL, id)
        then: 'event does not contain such username'
            response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            MusicEvent updatedEvent = eventRepository.findById(id)
            !updatedEvent.getPendingParticipants().contains(username)
            !updatedEvent.getParticipants().contains(username)
    }

    def 'users joined the event after owners of the event approval'() {
        given: 'event in database with join request'
            List<String> participants = ['Tom', 'Eva']
            MusicEvent event = eventRepository.save(EVENT2)
            participants.each { participant ->
                event.addPendingParticipant(participant)
            }
            String id = eventRepository.save(event).getId()
        when: 'owner accepts pending users'
            participants.each { participant ->
                sendAcceptRejectUserRequest(ACCEPT_USER_URL, id, participant)
            }
        then: 'participants join request approved'
            MusicEvent updatedEvent = eventRepository.findById(id)
            participants.each { participant ->
                !updatedEvent.getPendingParticipants().contains(participant)
                updatedEvent.getParticipants().contains(participant)
            }
    }

    def 'users removed from pending list when owner reject requests'() {
        given: 'event in database with join requests'
        List<String> participants = ['Tom', 'Eva']
        MusicEvent event = eventRepository.save(EVENT2)
        participants.each { participant ->
            event.addPendingParticipant(participant)
        }
        String id = eventRepository.save(event).getId()
        when: 'owner rejects pending users'
        participants.each { participant ->
            sendAcceptRejectUserRequest(REJECT_USER_URL, id, participant)
        }
        then: 'participants join request rejected'
        MusicEvent updatedEvent = eventRepository.findById(id)
        participants.each { participant ->
            !updatedEvent.getPendingParticipants().contains(participant)
        }
    }

    private def sendGetRequest(String url, String id) {
        mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('id', id))
    }

    private def sendAcceptRejectUserRequest(String url, String id, String username) {
        mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('id', id)
                .param('user', username))
    }

    private def responseListContainsUsers(ResultActions response, List<String> users) {
        def jsonResult = new JsonSlurper()
                .parseText(response
                .andReturn()
                .response
                .contentAsString)
        users.each { user ->
            if(!jsonResult.contains(user)) {
                return false
            }
        }
        return true
    }
}

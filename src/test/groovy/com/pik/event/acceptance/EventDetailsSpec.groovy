package com.pik.event.acceptance

import com.pik.base.MvcIntegrationSpec
import com.pik.common.InstrumentType
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

class EventDetailsSpec extends MvcIntegrationSpec{
    private static final String MODIFY_EVENT = '/api/event/modify'
    private static final String GET_EVENT_DETAILS = '/api/event/details'
    private static final MusicEvent EVENT = new MusicEvent(LocalDateTime.of(2016, 2, 10, 0, 0), 'Warsaw', 'Jam', 'Zosia')

    static {
        EVENT.addNeededInstrument(InstrumentType.DRUMS)
        EVENT.addParticipant('Damian')
        EVENT.setDescription('Jamming in garage.')
    }

    @Shared
    private String token
    @Autowired
    private EventRepository eventRepository

    def setup() {
        eventRepository.save(EVENT)
        def response = sendLoginRequest(
                testAccounts[1].getUsername(),
                testAccounts[1].getPassword())
        token = extractTokenFromResponse(response.andReturn().response.contentAsString)
    }

    def cleanup() {
        eventRepository.deleteAll()
    }

    def 'user receives description, needed instrument and participants names after sending a request'(){
        given: 'event exists in database and its id is known'
            def events = eventRepository.findByOwner('Zosia')
            events.size() == 1
            def id = events.get(0).getId()
        when: 'user sends request for event details'
            def response =  sendEventDetailsRequest(id)
        then:
            response.andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
            isCorrectEventDetailsResponse(response)
    }

    private def sendEventDetailsRequest(String id)
    {
        mockMvc.perform(get(GET_EVENT_DETAILS)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('id', id))
    }

    private def isCorrectEventDetailsResponse(ResultActions response)
    {
        def jsonResult = new JsonSlurper()
                .parseText(response
                .andReturn()
                .response
                .contentAsString)
        System.out.println()
        if (!jsonResult.description.equals(EVENT.description) ||
                !jsonResult.participants.get(0).equals(EVENT.participants.get(0)) ||
                !jsonResult.instrumentsNeeded.get(0).equals(EVENT.instrumentsNeeded.get(0).name))
            return false

        return true
    }
}

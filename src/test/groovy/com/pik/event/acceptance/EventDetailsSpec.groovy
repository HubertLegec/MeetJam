package com.pik.event.acceptance

import com.pik.base.MvcIntegrationSpec
import com.pik.common.InstrumentType
import com.pik.event.EventRepository
import com.pik.event.MusicEvent
import com.pik.event.details.UpdateDetailsDTO
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Shared

import java.time.LocalDateTime


import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class EventDetailsSpec extends MvcIntegrationSpec {
    private static final String EVENT_DETAILS_URL = '/api/event/details'
    private static final MusicEvent EVENT = new MusicEvent(LocalDateTime.of(2016, 2, 10, 0, 0), 'Warsaw', 'Jam', 'Zosia')

    static {
        EVENT.addNeededInstrument(InstrumentType.DRUMS)
        EVENT.addParticipant('Damian')
        EVENT.setDescription('Jamming in garage.')
    }

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

    def 'user receives description, needed instrument and participants names after sending a request'() {
        when: 'user sends request for event details'
            def response = sendEventDetailsRequest(eventID)
        then:
            response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            isCorrectEventDetailsResponse(response)
    }

    def 'user updates details of event he or she created'() {
        given: 'new event details'
            UpdateDetailsDTO dto = new UpdateDetailsDTO(eventID, 'New description', ['Guitar', 'Drums'])
        when: 'users sends request to update event details'
            def response = sendUpdateDetailsRequest(dto)
        then:
            response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            eventHasUpdatedDetails(dto)
    }

    private boolean eventHasUpdatedDetails(UpdateDetailsDTO dto){
        MusicEvent event = eventRepository.findById(eventID)
        if (event.description != dto.description) {
            return false
        }
        dto.instrumentsNeeded.each { instrumentName ->
            if(!event.instrumentsNeeded
                    .contains(InstrumentType.fromString(instrumentName))){
                return false
            }
        }
        return true
    }

    private def sendEventDetailsRequest(String id) {
        mockMvc.perform(get(EVENT_DETAILS_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('id', id))
    }

    private def sendUpdateDetailsRequest(UpdateDetailsDTO dto) {
        def json = JsonOutput.toJson(dto)
        mockMvc.perform(post(EVENT_DETAILS_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .content(json))
    }

    private def isCorrectEventDetailsResponse(ResultActions response) {
        def jsonResult = new JsonSlurper()
                .parseText(response
                .andReturn()
                .response
                .contentAsString)
        if (!jsonResult.description.equals(EVENT.description) ||
                !jsonResult.participants.get(0).equals(EVENT.participants.get(0)) ||
                !jsonResult.instrumentsNeeded.get(0).equals(EVENT.instrumentsNeeded.get(0).name))
            return false

        return true
    }


}

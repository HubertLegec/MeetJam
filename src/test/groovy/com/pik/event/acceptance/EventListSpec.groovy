package com.pik.event.acceptance

import com.pik.base.MvcIntegrationSpec
import com.pik.event.EventRepository
import com.pik.event.MusicEvent
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Shared

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class EventListSpec extends MvcIntegrationSpec {
    private static final String EVENT_LIST_URL = '/api/event/list'
    private static final String MY_EVENT_LIST_URL = '/api/event/myList'
    @Shared String token
    @Autowired
    private EventRepository eventRepository

    private static final List<MusicEvent> SAMPLE_EVENTS = [
            new MusicEvent(LocalDateTime.of(2016, 2, 10, 0, 0), 'Warsaw', 'First event', 'Zosia'),
            new MusicEvent(LocalDateTime.of(2016, 3, 10, 0, 0), 'Warsaw', 'Second event', 'Adam')
    ]

    def setup(){
        insertSampleEventsToDatabase()
        def response = sendLoginRequest(
                testAccounts[1].getUsername(),
                testAccounts[1].getPassword())
        token = extractTokenFromResponse(response.andReturn().response.contentAsString)
    }

    def cleanup(){
        eventRepository.deleteAll()
    }

    def 'should return list of recently added events in specified city'(){
        given: 'two sample events in Warsaw present in database'
            eventRepository.findAll().size() == 2
            def city = 'Warsaw'
            def dateFrom = LocalDateTime.of(2016, 01, 10, 0, 0)
            def dateTo = LocalDateTime.of(2016, 04, 10, 0, 0)
        when: 'user requested list of events in Warsaw'
            def response = sendEventListByCityRequest(token, city, dateFrom, dateTo)
        then: 'result list contains two sample events is returned'
            response.andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
            isCorrectEventList(response, SAMPLE_EVENTS)
    }

    def 'should return list of events created by specified user'(){
        given: 'one event created by Zosia present in database'
            eventRepository.findByOwner('Zosia').size() == 1
            def dateFrom = LocalDateTime.of(2016, 01, 20, 0, 0)
            def dateTo = LocalDateTime.of(2016, 02, 20, 0, 0)
        when: 'Zosia requested list of her events'
            def response = sendEventListByOwnerRequest(token, dateFrom, dateTo)
        then: 'result list contains one event with proper parameters'
            response.andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
            isCorrectEventList(response, SAMPLE_EVENTS.subList(0, 1))
    }

    def 'should return list of events which specified user joined'(){
        //TODO
    }

    def 'should return list of events in which is need for specified instrument player'(){
        //TODO
    }

    private def sendEventListByCityRequest(String token, String city, LocalDateTime dateFrom, LocalDateTime dateTo){
        mockMvc.perform(get(EVENT_LIST_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('city', city)
                .param('dateFrom', dateFrom.format(DateTimeFormatter.ISO_DATE_TIME))
                .param('dateTo', dateTo.format(DateTimeFormatter.ISO_DATE_TIME)))
    }

    private def sendEventListByOwnerRequest(String token, LocalDateTime dateFrom, LocalDateTime dateTo){
        mockMvc.perform(get(MY_EVENT_LIST_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('dateFrom', dateFrom.format(DateTimeFormatter.ISO_DATE_TIME))
                .param('dateTo', dateTo.format(DateTimeFormatter.ISO_DATE_TIME)))
    }

    private def sendEventListUserJoinedRequest(String token, LocalDateTime dateFrom, LocalDateTime dateTo){
        mockMvc.perform(get(EVENT_LIST_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('dateFrom', dateFrom.format(DateTimeFormatter.ISO_DATE_TIME))
                .param('dateTo', dateTo.format(DateTimeFormatter.ISO_DATE_TIME)))
    }

    private def insertSampleEventsToDatabase(){
        SAMPLE_EVENTS.each {
            eventRepository.save(it)
        }
    }

    private def isCorrectEventList(ResultActions response, List<MusicEvent> list){
        def jsonResult = new JsonSlurper().parseText(response.andReturn().response.contentAsString)
        for(int i = 0; i < list.size(); i++){
            if(!eventsEqual(jsonResult[i], list[i])){
                return false
            }
        }
        return true
    }

    private boolean eventsEqual(def fromJson, MusicEvent event){
        if(event.owner != fromJson.owner){
            return false
        }
        if(event.title != fromJson.title){
            return false
        }
        if(event.city != fromJson.city){
            return false
        }
        if(!event.date.isEqual(LocalDateTime.parse(fromJson.date))){
            return false
        }
        return true
    }
}

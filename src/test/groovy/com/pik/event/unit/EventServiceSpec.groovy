package com.pik.event.unit

import com.pik.event.exception.EventException
import com.pik.event.exception.EventNotFoundException
import com.pik.event.exception.EventsError
import com.pik.event.MusicEvent
import com.pik.event.createremove.CreateEventDTO
import com.pik.event.createremove.CreateEventResultDTO
import com.pik.event.createremove.CreateRemoveEventService

import java.time.LocalDateTime

class EventServiceSpec extends EventServiceBaseSpec {

    private CreateRemoveEventService eventService

    def setup(){
        eventRepository.findByOwnerAndDateBetween(_ , _ , _) >>
                { String owner, LocalDateTime from, LocalDateTime to ->
                    return SAMPLE_EVENTS.findAll{
                        (owner == null || it.owner == owner) &&
                                (to == null || it.date.isBefore(to)) &&
                                (from == null || it.date.isAfter(from))
                    }
                }
        eventRepository.findByCityAndInstrumentNeededAndDateBetween(_, _, _, _) >>
                { String city, String instrument, LocalDateTime from, LocalDateTime to ->
                    return SAMPLE_EVENTS.findAll{
                        (city == null || it.city == city) &&
                                (to == null || it.date.isBefore(to)) &&
                                (from == null || it.date.isAfter(from)) &&
                                (instrument == null || it.instrumentsNeeded.contains(instrument))
                    }
                }
        eventRepository.findByParticipantsInAndDateBetween(_, _, _) >>
                {
                    String participant, LocalDateTime from, LocalDateTime to ->
                        return SAMPLE_EVENTS.findAll{
                            (to == null || it.date.isBefore(to)) &&
                                    (from == null || it.date.isAfter(from)) &&
                                    (participant == null || it.participants.contains(participant))
                        }
                }
        eventRepository.save(_) >> { MusicEvent event ->
                    MusicEvent result = new MusicEvent(event.date, event.city, event.title, event.owner)
                    result.id = '123456'
                    return result
                }
        eventService = new CreateRemoveEventService(eventRepository, tokenHandler)
    }

    def 'should return dto with id when event is created'(){
        given: 'event details'
            CreateEventDTO dto = new CreateEventDTO('Warsaw', LocalDateTime.of(2016, 4, 22, 16, 30), 'title');
            String token = 'aaaabbbbccc'
        when: 'create event method is called'
            CreateEventResultDTO result = eventService.createEvent(token, dto)
        then: 'result contains event id'
            result.id != null && result.id.length() > 0
    }

    def 'token should be null and response should contain errors if creating event input is invalid'(){
        given:
            String token = 'aaaabbbbccc'
            CreateEventDTO dto = new CreateEventDTO(null, LocalDateTime.of(2016, 4, 22, 16, 30), '')
        when: 'create event method is called with incorrect parameters'
            CreateEventResultDTO result = eventService.createEvent(token, dto)
        then: 'id is null and response contains errors'
            result.id == null
            result.messages.contains(EventsError.EMPTY_CITY_FIELD.message)
            result.messages.contains(EventsError.EMPTY_TITLE_FILED.message)
    }

    def 'execution should finish in normal way if input is correct'(){
        given: 'event present in database and id is given'
            eventRepository.findById(_) >> SAMPLE_EVENTS[0]
            String id = '12345abc'
            String token = 'wetw3rew'
        when: 'owner removes event'
            eventService.removeEvent(token, id)
        then:
            final EventException exception = notThrown()
    }

    def 'exception should be thrown when user try to remove not existing event'(){
        given: 'id is given but event is not present in database'
            eventRepository.findById(_) >> null
            String id = '12345abc'
            String token = 'wetw3rew'
        when:
            eventService.removeEvent(token, id)
        then:
            final EventNotFoundException exception = thrown()
    }

    def 'exception should be thrown when user try to remove not her event'(){
        given: 'id is given adn sample events present in database'
            eventRepository.findById(_) >> SAMPLE_EVENTS[1]
            String id = '12345abc'
            String token = 'wetw3rew'
        when:
            eventService.removeEvent(token, id)
        then:
            final EventException exception = thrown()
            exception.message == EventsError.NOT_USERS_EVENT.message
    }
}

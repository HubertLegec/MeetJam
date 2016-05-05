package com.pik.event.unit

import com.pik.account.Authority
import com.pik.event.CreateEventResultDTO
import com.pik.event.EventDTO
import com.pik.event.EventRepository
import com.pik.event.EventService
import com.pik.event.EventsError
import com.pik.event.MusicEvent
import com.pik.event.RemoveEventException
import com.pik.security.TokenHandler
import org.springframework.security.core.userdetails.User
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventServiceSpec extends Specification {
    private static final List<MusicEvent> SAMPLE_EVENTS = [
            new MusicEvent(LocalDateTime.of(2016, 2, 10, 0, 0), 'Warsaw', 'First event', 'Zosia'),
            new MusicEvent(LocalDateTime.of(2016, 3, 10, 0, 0), 'Warsaw', 'Second event', 'Adam'),
            new MusicEvent(LocalDateTime.of(2016, 1, 22, 0, 0), 'Warsaw', 'Third event', 'Zosia'),
            new MusicEvent(LocalDateTime.of(2016, 2, 5, 0, 0), 'Cracow', 'Fourth event', 'Adam'),
    ]

    static {
        SAMPLE_EVENTS[1].addParticipant('Zosia')
        SAMPLE_EVENTS[3].addParticipant('Zosia')
    }

    private EventService eventService
    private EventRepository eventRepository
    private TokenHandler tokenHandler

    def setup(){
        eventRepository = Mock()
        tokenHandler = Mock()
        tokenHandler.parseUserFromToken(_) >> new User('Zosia', 'zosia123', [new Authority('USER')])
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
        eventService = new EventService(eventRepository, tokenHandler)
    }


    def 'should return event list for given owner and dates range'(){
        given: 'dates range, token recognized as Zosia'
            String token = 'aaaabbbccc'
            String dateFrom = LocalDateTime.of(2016, 2, 2, 0, 0)
                    .format(DateTimeFormatter.ISO_DATE_TIME)
            String dateTo = LocalDateTime.of(2016, 3, 2, 0, 0)
                    .format(DateTimeFormatter.ISO_DATE_TIME)
        when:
            List<EventDTO> result = eventService.fetchEventListByOwner(token, dateFrom, dateTo)
        then:
            result.size() == 1
            eventAndDTOequals(SAMPLE_EVENTS[0], result[0])
    }

    def 'should return all the events for given owner when dates range is not specified'(){
        given:
            String token = 'aaaabbbccc'
            String dateFrom = null
            String dateTo = null
        when:
            List<EventDTO> result = eventService.fetchEventListByOwner(token, dateFrom, dateTo)
        then:
            result.size() == 2
            resultContainsEvent(result, SAMPLE_EVENTS[0])
            resultContainsEvent(result, SAMPLE_EVENTS[2])
    }

    def 'should return event list for given city and dates range'(){
        given:
            String dateFrom = LocalDateTime.of(2016, 1, 10, 0, 0)
                    .format(DateTimeFormatter.ISO_DATE_TIME)
            String dateTo = LocalDateTime.of(2016, 2, 22, 0, 0)
                    .format(DateTimeFormatter.ISO_DATE_TIME)
            String city = 'Warsaw'
        when:
            List<EventDTO> result = eventService.fetchEventsByCityAndInstrumentAndDate(city, null, dateFrom, dateTo)
        then:
            result.size() == 2
            resultContainsEvent(result, SAMPLE_EVENTS[0])
            resultContainsEvent(result, SAMPLE_EVENTS[2])
    }

    def 'should return all the events for given city when dates range is not specified'(){
        given:
            String dateFrom = null
            String dateTo = null
            String city = 'Warsaw'
        when:
            List<EventDTO> result = eventService.fetchEventsByCityAndInstrumentAndDate(city, null, dateFrom, dateTo)
        then:
            result.size() == 3
            resultContainsEvent(result, SAMPLE_EVENTS[0])
            resultContainsEvent(result, SAMPLE_EVENTS[1])
            resultContainsEvent(result, SAMPLE_EVENTS[2])
    }

    def 'should return empty list for data range when there are no events'(){
        given:
            String dateFrom = LocalDateTime.of(2016, 4, 10, 0, 0)
                .format(DateTimeFormatter.ISO_DATE_TIME)
            String dateTo = LocalDateTime.of(2016, 5, 22, 0, 0)
                .format(DateTimeFormatter.ISO_DATE_TIME)
            String city = 'Warsaw'
        when:
            List<EventDTO> result = eventService.fetchEventsByCityAndInstrumentAndDate(city, null, dateFrom, dateTo)
        then:
            result.size() == 0
    }

    def 'should return empty list for city where there are no events'(){
        given:
            String dateFrom = LocalDateTime.of(2016, 4, 10, 0, 0)
                .format(DateTimeFormatter.ISO_DATE_TIME)
            String dateTo = LocalDateTime.of(2016, 5, 22, 0, 0)
                .format(DateTimeFormatter.ISO_DATE_TIME)
            String city = 'Lublin'
        when:
            List<EventDTO> result = eventService.fetchEventsByCityAndInstrumentAndDate(city, null, dateFrom, dateTo)
        then:
            result.size() == 0
    }

    def 'should return event list for given participant and dates range'(){
        given:
            String token = 'aaaabbbccc'
            String dateFrom = LocalDateTime.of(2016, 2, 2, 0, 0)
                    .format(DateTimeFormatter.ISO_DATE_TIME)
            String dateTo = LocalDateTime.of(2016, 3, 2, 0, 0)
                    .format(DateTimeFormatter.ISO_DATE_TIME)
        when:
            List<EventDTO> result = eventService.fetchJoinedEventList(token, dateFrom, dateTo)
        then:
            result.size() == 1
            resultContainsEvent(result, SAMPLE_EVENTS[3])
    }

    def 'should return empty list for given participant and dates range when there are no events'(){
        given:
            String token = 'aaaabbbccc'
            String dateFrom = LocalDateTime.of(2016, 3, 22, 0, 0)
                .format(DateTimeFormatter.ISO_DATE_TIME)
            String dateTo = LocalDateTime.of(2016, 4, 2, 0, 0)
                .format(DateTimeFormatter.ISO_DATE_TIME)
        when:
            List<EventDTO> result = eventService.fetchJoinedEventList(token, dateFrom, dateTo)
        then:
            result.size() == 0
    }

    def 'should return event list for given participant containing all events he/she joined when dates range is not specified'(){
        given:
            String token = 'aaaabbbccc'
            String dateFrom = null
            String dateTo = null
        when:
            List<EventDTO> result = eventService.fetchJoinedEventList(token, dateFrom, dateTo)
        then:
            result.size() == 2
            resultContainsEvent(result, SAMPLE_EVENTS[1])
            resultContainsEvent(result, SAMPLE_EVENTS[3])
    }

    def 'should return dto with id when event is created'(){
        given: 'event details'
            String token = 'aaaabbbbccc'
            String title = 'title'
            String city = 'Warsaw'
            LocalDateTime dateTime = LocalDateTime.of(2016, 4, 22, 16, 30)
        when: 'create event method is called'
            CreateEventResultDTO result = eventService.createEvent(token, title, city, dateTime.format(DateTimeFormatter.ISO_DATE_TIME))
        then: 'result contains event id'
            result.id != null && result.id.length() > 0
    }

    def 'token should be null and response should contain errors if creating event input is invalid'(){
        given:
            String token = 'aaaabbbbccc'
            String title = ''
            String city = null
            String dateTime = 'abs'
        when: 'create event method is called with incorrect parameters'
            CreateEventResultDTO result = eventService.createEvent(token, title, city, dateTime)
        then: 'id is null and response contains errors'
            result.id == null
            result.messages.contains(EventsError.EMPTY_CITY_FIELD.message)
            result.messages.contains(EventsError.INCORRECT_DATE.message)
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
            final RemoveEventException exception = notThrown()
    }

    def 'exception should be thrown when user try to remove not existing event'(){
        given: 'id is given but event is not present in database'
            eventRepository.findById(_) >> null
            String id = '12345abc'
            String token = 'wetw3rew'
        when:
            eventService.removeEvent(token, id)
        then:
            final RemoveEventException exception = thrown()
            exception.message == EventsError.EVENT_DOES_NOT_EXIST.message
    }

    def 'exception should be thrown when user try to remove not her event'(){
        given: 'id is given adn sample events present in database'
        eventRepository.findById(_) >> SAMPLE_EVENTS[1]
        String id = '12345abc'
        String token = 'wetw3rew'
        when:
        eventService.removeEvent(token, id)
        then:
        final RemoveEventException exception = thrown()
        exception.message == EventsError.NOT_USERS_EVENT.message
    }


    private boolean eventAndDTOequals(MusicEvent musicEvent, EventDTO dto){
        return dto.city == musicEvent.city &&
                dto.owner == musicEvent.owner &&
                dto.title == musicEvent.title &&
                dto.convertedDate.isEqual(musicEvent.date)
    }

    private boolean resultContainsEvent(List<EventDTO> result, MusicEvent event){
        for(EventDTO dto : result){
            if(eventAndDTOequals(event, dto)){
                return true
            }
        }
        return false
    }
}

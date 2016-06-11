package com.pik.event.unit

import com.pik.event.exception.EventException
import com.pik.event.exception.EventsError
import com.pik.event.participants.EventParticipantsService

class EventParticipantsServiceSpec extends EventServiceBaseSpec {

    private EventParticipantsService eventParticipantsService

    def setup() {
        eventRepository.findById(_) >> {
            String id ->
                return SAMPLE_EVENTS.find { it -> it.id == id}
        }
        eventRepository.findByParticipantsIn(_) >> {
            String login ->
                return SAMPLE_EVENTS.findAll { it -> it.participants.contains(login)}
        }
        eventRepository.findByPendingParticipantsIn(_) >> {
            String login ->
                return SAMPLE_EVENTS.findAll { it -> it.pendingParticipants.contains(login)}
        }
        eventParticipantsService = new EventParticipantsService(eventRepository, tokenHandler)
    }

    def 'user can join other events'() {
        given: 'event id user has joined before and authenticated user'
            String id = 'a4'
            String token = 'zosia12323zosia'
        when: 'user joins event'
            eventParticipantsService.joinEvent(token, id)
        then: 'operation was successful and no exception was thrown'
            noExceptionThrown()
    }

    def 'user can not join own event' () {
        given: 'event id and authenticated user'
            String id = 'a3'
            String token = 'zosia12323zosia'
        when: 'user joins event'
            eventParticipantsService.joinEvent(token, id)
        then: 'event exception with information is thrown'
            final EventException eventException = thrown()
            eventException.message == EventsError.CAN_NOT_JOIN_OWN_EVENT.message
    }

    def 'user can not join event user has already joined'(){
        given: 'event id and authenticated user'
            String id = 'a2'
            String token = 'zosia12323zosia'
        when: 'user joins event'
            eventParticipantsService.joinEvent(token, id)
        then: 'event exception with information is thrown'
            final EventException eventException = thrown()
            eventException.message == EventsError.USER_ALREADY_JOINED.message
    }

    def 'user can not join event for which waiting for approval'() {
        given: 'event id user is waiting for approval to join and authenticated user'
            String id = 'a4'
            SAMPLE_EVENTS[3].addPendingParticipant('Zosia')
            String token = 'zosia12323zosia'
        when: 'user joins event'
            eventParticipantsService.joinEvent(token, id)
        then: 'event exception with information is thrown'
            final EventException eventException = thrown()
            eventException.message == EventsError.USER_IS_WAITING_FOR_APPROVAL.message
    }

}

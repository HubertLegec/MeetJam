package com.pik.event.unit

import com.pik.account.Authority
import com.pik.event.EventRepository
import com.pik.event.MusicEvent
import com.pik.security.TokenHandler
import org.springframework.security.core.userdetails.User
import spock.lang.Specification

import java.time.LocalDateTime

abstract class EventServiceBaseSpec extends Specification{
    protected static final List<MusicEvent> SAMPLE_EVENTS = [
            new MusicEvent(LocalDateTime.of(2016, 2, 10, 0, 0), 'Warsaw', 'First event', 'Zosia'),
            new MusicEvent(LocalDateTime.of(2016, 3, 10, 0, 0), 'Warsaw', 'Second event', 'Adam'),
            new MusicEvent(LocalDateTime.of(2016, 1, 22, 0, 0), 'Warsaw', 'Third event', 'Zosia'),
            new MusicEvent(LocalDateTime.of(2016, 2, 5, 0, 0), 'Cracow', 'Fourth event', 'Adam'),
    ]

    static {
        SAMPLE_EVENTS[1].addParticipant('Zosia')
        SAMPLE_EVENTS[0].id = 'a1'
        SAMPLE_EVENTS[1].id = 'a2'
        SAMPLE_EVENTS[2].id = 'a3'
        SAMPLE_EVENTS[3].id = 'a4'
    }

    protected EventRepository eventRepository
    protected TokenHandler tokenHandler

    def setup(){
        eventRepository = Mock()
        tokenHandler = Mock()
        tokenHandler.parseUserFromToken(_) >> new User('Zosia', 'zosia123', [new Authority('USER')])
    }
}

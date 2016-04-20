package com.pik.event;


import com.pik.common.InstrumentType;
import com.pik.security.TokenHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class EventService {
    private EventRepository eventRepository;
    private TokenHandler tokenHandler;

    public EventService(EventRepository eventRepository, TokenHandler tokenHandler) {
        this.eventRepository = eventRepository;
        this.tokenHandler = tokenHandler;
    }

    public List<EventDTO> fetchEventsByCity(String city, String dateFrom, String dateTo){
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        List<MusicEvent> eventsByCity = eventRepository.findByCity(city);
        return filterEventsByDateAndTransformToDTO(eventsByCity, parsedFrom, parsedTo);
    }

    public List<EventDTO> fetchEventListByOwner(String token, String dateFrom, String dateTo){
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        String owner = getLoginFromToken(token);
        if(owner == null){
            return new ArrayList<>();
        }
        List<MusicEvent> ownerEvents = eventRepository.findByOwner(owner);
        return filterEventsByDateAndTransformToDTO(ownerEvents, parsedFrom, parsedTo);
    }

    public List<EventDTO> fetchJoinedEventList(String token, String dateFrom, String dateTo){
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        String participant = getLoginFromToken(token);
        if(participant == null){
            return new ArrayList<>();
        }
        List<MusicEvent> userEvents = eventRepository.findByParticipantsIn(participant);
        return filterEventsByDateAndTransformToDTO(userEvents, parsedFrom, parsedTo);
    }

    public List<EventDTO> fetchEventListByInstrumentNeeded(String instrument, String dateFrom, String dateTo){
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        InstrumentType instrumentType = InstrumentType.fromString(instrument);
        if(instrument == null){
            return new ArrayList<>();
        }
        List<MusicEvent> eventsByInstrument = eventRepository.findByInstrumentsNeededIn(instrumentType);
        return filterEventsByDateAndTransformToDTO(eventsByInstrument, parsedFrom, parsedTo);
    }

    private LocalDateTime parseDateTime(String value){
        if(value == null){
            return null;
        }
        try {
            LocalDateTime parsed = LocalDateTime.parse(value);
            return parsed;
        } catch (DateTimeParseException e){
            return null;
        }
    }

    private boolean dateBetween(LocalDateTime from, LocalDateTime to, LocalDateTime toCheck){
        if(from == null && to == null){
            return true;
        } else if(to == null){
            return toCheck.isAfter(from);
        } else if(from == null){
            return toCheck.isBefore(to);
        }
        return toCheck.isAfter(from) && toCheck.isBefore(to);
    }

    private String getLoginFromToken(String token){
        try{
            return tokenHandler.parseUserFromToken(token)
                    .getUsername();
        } catch (UsernameNotFoundException e){
            return null;
        }
    }

    private List<EventDTO> filterEventsByDateAndTransformToDTO(List<MusicEvent> events, LocalDateTime dateFrom, LocalDateTime dateTo){
        return events
                .stream()
                .filter( it -> dateBetween(dateFrom, dateTo, it.getDate()))
                .map( it -> new EventDTO(it))
                .collect(toList());
    }
}

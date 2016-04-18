package com.pik.event;


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
        //TODO
        return getMockedEventList()
                .stream()
                .filter( it ->
                        it.getCity().equals(city)
                        && dateBetween(parsedFrom, parsedTo, it.getConvertedDate()))
                .collect(toList());
    }

    public List<EventDTO> fetchEventListByOwner(String token, String dateFrom, String dateTo){
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        String owner = getLoginFromToken(token);
        if(owner == null){
            return new ArrayList<>();
        }
        //TODO
        return getMockedEventList()
                .stream()
                .filter( it ->
                        it.getOwner().equals(owner)
                                && dateBetween(parsedFrom, parsedTo, it.getConvertedDate()))
                .collect(toList());
    }

    public List<EventDTO> fetchJoinedEventList(String token, String dateFrom, String dateTo){
        //TODO
        return new ArrayList<>();
    }

    public List<EventDTO> fetchEventListByInstrumentNeeded(String instrument, String dateFrom, String dateTo){
        //TODO
        return new ArrayList<>();
    }


    private List<EventDTO> getMockedEventList(){
        List<EventDTO> mockedList = new ArrayList<>();
        mockedList.add(new EventDTO("Zosia", "First event", "Warsaw", LocalDateTime.of(2016, 2, 10, 0, 0)));
        mockedList.add(new EventDTO("Adam", "Second event", "Warsaw", LocalDateTime.of(2016, 3, 10, 0, 0)));

        return mockedList;
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
}

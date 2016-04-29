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

    public List<EventDTO> fetchEventsByCityAndInstrumentAndDate(String city, String instrument, String dateFrom, String dateTo){
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        InstrumentType instrumentNeeded = InstrumentType.fromString(instrument);
        List<MusicEvent> eventsByCity = eventRepository.findByCityAndInstrumentNeededAndDateBetween(city,instrumentNeeded,parsedFrom,parsedTo);
        return transformToEventDTOList(eventsByCity);
    }

    public List<EventDTO> fetchEventListByOwner(String token, String dateFrom, String dateTo){
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        String owner = getLoginFromToken(token);
        if(owner == null){
            return new ArrayList<>();
        }
        List<MusicEvent> ownerEvents = eventRepository.findByOwnerAndDateBetween(owner, parsedFrom, parsedTo);
        return transformToEventDTOList(ownerEvents);
    }

    public List<EventDTO> fetchJoinedEventList(String token, String dateFrom, String dateTo){
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        String participant = getLoginFromToken(token);
        if(participant == null){
            return new ArrayList<>();
        }
        List<MusicEvent> userEvents = eventRepository.findByParticipantsInAndDateBetween(participant,parsedFrom,parsedTo);
        return transformToEventDTOList(userEvents);
    }

    public CreateEventResultDTO createEvent(String token, String title, String city, String date){
        String owner = getLoginFromToken(token);
        LocalDateTime dateTime = parseDateTime(date);
        List<String> validationMessages = vailidateInput(owner, title, city, dateTime);
        if(validationMessages.size() > 0){
            return new CreateEventResultDTO(validationMessages);
        }

        MusicEvent event = new MusicEvent(dateTime, city, title, owner);
        MusicEvent result = eventRepository.save(event);
        return new CreateEventResultDTO(result.getId());
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

    private String getLoginFromToken(String token){
        try{
            return tokenHandler.parseUserFromToken(token)
                    .getUsername();
        } catch (UsernameNotFoundException e){
            return null;
        }
    }

    private List<EventDTO> transformToEventDTOList(List<MusicEvent> events){
        return events
                .stream()
                .map( it -> new EventDTO(it))
                .collect(toList());
    }

    private List<String> vailidateInput(String user, String title, String city, LocalDateTime date){
        List<String> result = new ArrayList<>();
        if(user == null || user.length() == 0){
            result.add(EventsError.USERNAME_ERROR.getMessage());
        }
        if(title == null || title.length() == 0){
            result.add(EventsError.EMPTY_TITLE_FILED.getMessage());
        }
        if(city == null || city.length() == 0){
            result.add(EventsError.EMPTY_CITY_FIELD.getMessage());
        }
        if(date == null){
            result.add(EventsError.INCORRECT_DATE.getMessage());
        }
        return result;
    }
}

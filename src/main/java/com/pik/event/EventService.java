package com.pik.event;

import com.pik.common.InstrumentType;
import com.pik.security.TokenHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.pik.event.EventsError.*;
import static java.util.stream.Collectors.toList;


public class EventService {
    private EventRepository eventRepository;
    private TokenHandler tokenHandler;

    public EventService(EventRepository eventRepository, TokenHandler tokenHandler) {
        this.eventRepository = eventRepository;
        this.tokenHandler = tokenHandler;
    }

    public List<EventDTO> fetchEventsByCityAndInstrumentAndDate(String city, String instrument, String dateFrom, String dateTo) {
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        InstrumentType instrumentNeeded = InstrumentType.fromString(instrument);
        List<MusicEvent> eventsByCity = eventRepository
                .findByCityAndInstrumentNeededAndDateBetween(city, instrumentNeeded, parsedFrom, parsedTo);
        return transformToEventDTOList(eventsByCity);
    }

    public List<EventDTO> fetchEventListByOwner(String token, String dateFrom, String dateTo) {
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        String owner = getLoginFromToken(token);
        if (owner == null) {
            return new ArrayList<>();
        }
        List<MusicEvent> ownerEvents = eventRepository
                .findByOwnerAndDateBetween(owner, parsedFrom, parsedTo);
        return transformToEventDTOList(ownerEvents);
    }

    public List<EventDTO> fetchJoinedEventList(String token, String dateFrom, String dateTo) {
        LocalDateTime parsedFrom = parseDateTime(dateFrom);
        LocalDateTime parsedTo = parseDateTime(dateTo);
        String participant = getLoginFromToken(token);
        if (participant == null) {
            return new ArrayList<>();
        }
        List<MusicEvent> userEvents = eventRepository
                .findByParticipantsInAndDateBetween(participant, parsedFrom, parsedTo);
        return transformToEventDTOList(userEvents);
    }

    public CreateEventResultDTO createEvent(String token, String title, String city, String date) {
        String owner = getLoginFromToken(token);
        LocalDateTime dateTime = parseDateTime(date);
        List<String> validationMessages = validateCreateInput(owner, title, city, dateTime);
        if (validationMessages.size() > 0) {
            return new CreateEventResultDTO(validationMessages);
        }

        MusicEvent event = new MusicEvent(dateTime, city, title, owner);
        MusicEvent result = eventRepository.save(event);
        return new CreateEventResultDTO(result.getId());
    }

    public EventDetailsDTO getEventDetails(String id)
    {
        MusicEvent event = eventRepository.findById(id);
        if (event == null){
            throw new EventNotFoundException();
        }
        return new EventDetailsDTO(event.getDescription(),
                                    event.getInstrumentsNeeded().stream().map(InstrumentType::getName).collect(toList()),
                                    event.getParticipants());
    }


    public void removeEvent(String token, String id) throws RemoveEventException {
        String owner = getLoginFromToken(token);
        validateRemoveInput(owner, id);
        MusicEvent eventToRemove = eventRepository.findById(id);
        validateRemoval(owner, eventToRemove);
        eventRepository.delete(eventToRemove);
    }


    private LocalDateTime parseDateTime(String value) {
        if (value == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private String getLoginFromToken(String token) {
        try {
            return tokenHandler.parseUserFromToken(token)
                    .getUsername();
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }

    private List<EventDTO> transformToEventDTOList(List<MusicEvent> events) {
        return events
                .stream()
                .map(EventDTO::new)
                .collect(toList());
    }

    private List<String> validateCreateInput(String user, String title, String city, LocalDateTime date) {
        List<String> result = new ArrayList<>();
        if (user == null || user.length() == 0) {
            result.add(USERNAME_ERROR.getMessage());
        }
        if (title == null || title.length() == 0) {
            result.add(EMPTY_TITLE_FILED.getMessage());
        }
        if (city == null || city.length() == 0) {
            result.add(EMPTY_CITY_FIELD.getMessage());
        }
        if (date == null) {
            result.add(INCORRECT_DATE.getMessage());
        }
        return result;
    }

    private void validateRemoveInput(String user, String id) throws RemoveEventException {
        if (user == null || user.length() == 0) {
            throw new RemoveEventException(USERNAME_ERROR.getMessage(), HttpStatus.NOT_FOUND);
        }
        if (id == null || id.length() == 0) {
            throw new RemoveEventException(EVENT_ID_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void validateRemoval(String user, MusicEvent event) throws RemoveEventException {
        if (event == null) {
            throw new RemoveEventException(EVENT_DOES_NOT_EXIST.getMessage(), HttpStatus.NOT_FOUND);
        }
        if (!user.equals(event.getOwner())) {
            throw new RemoveEventException(NOT_USERS_EVENT.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}

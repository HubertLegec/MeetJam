package com.pik.event.createremove;

import static com.pik.event.EventsError.*;

import com.pik.event.EventRepository;
import com.pik.event.MusicEvent;
import com.pik.security.TokenHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class CreateRemoveEventService {
    private EventRepository eventRepository;
    private TokenHandler tokenHandler;

    public CreateRemoveEventService(EventRepository eventRepository, TokenHandler tokenHandler) {
        this.eventRepository = eventRepository;
        this.tokenHandler = tokenHandler;
    }

    public CreateEventResultDTO createEvent(String token, CreateEventDTO input) {
        String owner = getLoginFromToken(token);
        List<String> validationMessages = validateCreateInput(owner, input.getTitle(), input.getCity(), input.getDate());
        if (validationMessages.size() > 0) {
            return new CreateEventResultDTO(validationMessages);
        }

        MusicEvent event = new MusicEvent(input.getDate(), input.getCity(), input.getTitle(), owner);
        MusicEvent result = eventRepository.save(event);
        return new CreateEventResultDTO(result.getId());
    }

    public void removeEvent(String token, String id) throws RemoveEventException {
        String owner = getLoginFromToken(token);
        validateRemoveInput(owner, id);
        MusicEvent eventToRemove = eventRepository.findById(id);
        validateRemoval(owner, eventToRemove);
        eventRepository.delete(eventToRemove);
    }

    private String getLoginFromToken(String token) {
        try {
            return tokenHandler.parseUserFromToken(token)
                    .getUsername();
        } catch (UsernameNotFoundException e) {
            return null;
        }
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

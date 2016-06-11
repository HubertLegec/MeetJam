package com.pik.event.createremove;

import com.pik.event.BaseEventService;
import com.pik.event.EventRepository;
import com.pik.event.MusicEvent;
import com.pik.event.exception.EventException;
import com.pik.security.TokenHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pik.event.exception.EventsError.*;


public class CreateRemoveEventService extends BaseEventService {

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

    public void removeEvent(String token, String id) throws EventException {
        String owner = getLoginFromToken(token);
        validateInput(id);
        MusicEvent eventToRemove = eventRepository.findById(id);
        validateEvent(eventToRemove);
        validatePrivileges(owner, eventToRemove);
        eventRepository.delete(eventToRemove);
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


}

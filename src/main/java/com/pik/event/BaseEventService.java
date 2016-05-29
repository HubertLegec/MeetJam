package com.pik.event;

import com.pik.security.TokenHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.pik.event.EventsError.*;

public class BaseEventService {
    protected EventRepository eventRepository;
    protected TokenHandler tokenHandler;

    protected void validateInput(String user, String id) {
        if (user == null || user.length() == 0) {
            throw new EventException(USERNAME_ERROR.getMessage(), HttpStatus.NOT_FOUND);
        }
        if (id == null || id.length() == 0) {
            throw new EventException(EVENT_ID_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    protected void validateEvent(MusicEvent event){
        if (event == null) {
            throw new EventNotFoundException();
        }
    }

    protected void validatePrivileges(String user, MusicEvent event) {
        if (!user.equals(event.getOwner())) {
            throw new EventException(NOT_USERS_EVENT.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    protected String getLoginFromToken(String token) {
        try {
            return tokenHandler.parseUserFromToken(token)
                    .getUsername();
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }
}

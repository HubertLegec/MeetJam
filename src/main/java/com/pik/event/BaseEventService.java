package com.pik.event;

import com.pik.event.exception.EventException;
import com.pik.event.exception.EventNotFoundException;
import com.pik.security.TokenHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.pik.event.exception.EventsError.EVENT_ID_ERROR;
import static com.pik.event.exception.EventsError.NOT_USERS_EVENT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class BaseEventService {
    protected EventRepository eventRepository;
    protected TokenHandler tokenHandler;

    protected void validateInput(String id) {
        if (id == null || id.length() == 0) {
            throw new EventException(EVENT_ID_ERROR, BAD_REQUEST);
        }
    }

    protected void validateEvent(MusicEvent event){
        if (event == null) {
            throw new EventNotFoundException();
        }
    }

    protected void validatePrivileges(String user, MusicEvent event) {
        if (!user.equals(event.getOwner())) {
            throw new EventException(NOT_USERS_EVENT, FORBIDDEN);
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

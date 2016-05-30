package com.pik.event.participants;

import com.pik.event.BaseEventService;
import com.pik.event.EventException;
import com.pik.event.EventRepository;
import com.pik.event.MusicEvent;
import com.pik.security.TokenHandler;
import org.springframework.http.HttpStatus;
import java.util.List;


public class EventParticipantsService extends BaseEventService{

    public EventParticipantsService(EventRepository eventRepository, TokenHandler tokenHandler) {
        this.eventRepository = eventRepository;
        this.tokenHandler = tokenHandler;
    }

    public void joinEvent(String token, String id){
        String user = getLoginFromToken(token);
        validateInput(id);
        MusicEvent event = eventRepository.findById(id);
        validateEvent(event);
        checkIfJoinedBefore(event, user);
        event.addPendingParticipant(user);
        eventRepository.save(event);
    }

    public void leaveEvent(String token, String id){
        String user = getLoginFromToken(token);
        validateInput(id);
        MusicEvent event = eventRepository.findById(id);
        validateEvent(event);
        event.removeParticipant(user);
        eventRepository.save(event);
    }

    public void acceptUser(String token, String id, String userName){
        String owner = getLoginFromToken(token);
        validateInput(id);
        MusicEvent event = eventRepository.findById(id);
        validateEvent(event);
        validatePrivileges(owner, event);
        if(!event.getPendingParticipants().contains(userName)){
            throw new EventException("No such user waits for approval", HttpStatus.NOT_FOUND);
        }
        event.acceptUser(userName);
        eventRepository.save(event);
    }

    public void rejectUser(String token, String id, String userName){
        String owner = getLoginFromToken(token);
        validateInput(id);
        MusicEvent event = eventRepository.findById(id);
        validateEvent(event);
        validatePrivileges(owner, event);
        if(!event.getPendingParticipants().contains(userName)){
            throw new EventException("No such user waits for approval", HttpStatus.NOT_FOUND);
        }
        event.rejectUser(userName);
        eventRepository.save(event);
    }

    public List<String> getPendingUsers(String token, String id){
        String owner = getLoginFromToken(token);
        validateInput(id);
        MusicEvent event = eventRepository.findById(id);
        validateEvent(event);
        validatePrivileges(owner, event);
        return event.getPendingParticipants();
    }

    private void checkIfJoinedBefore(MusicEvent event, String userName){
        if(event.getParticipants().contains(userName)){
            throw new EventException("Usser has already joined this course", HttpStatus.CONFLICT);
        }
        if (event.getPendingParticipants().contains(userName)){
            throw new EventException("User is waiting for owner's approval", HttpStatus.CONFLICT);
        }
    }
}

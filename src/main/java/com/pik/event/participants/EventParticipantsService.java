package com.pik.event.participants;

import com.pik.event.BaseEventService;
import com.pik.event.EventException;
import com.pik.event.EventRepository;
import com.pik.event.MusicEvent;
import com.pik.security.TokenHandler;
import org.springframework.http.HttpStatus;

import java.util.List;

import static java.util.stream.Collectors.toList;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;


public class EventParticipantsService extends BaseEventService{

    public EventParticipantsService(EventRepository eventRepository, TokenHandler tokenHandler) {
        this.eventRepository = eventRepository;
        this.tokenHandler = tokenHandler;
    }

    void joinEvent(String token, String id){
        String user = getLoginFromToken(token);
        validateInput(id);
        MusicEvent event = eventRepository.findById(id);
        validateEvent(event);
        checkIfOwnEvent(event, user);
        checkIfJoinedBefore(event, user);
        event.addPendingParticipant(user);
        eventRepository.save(event);
    }

    void leaveEvent(String token, String id){
        String user = getLoginFromToken(token);
        validateInput(id);
        MusicEvent event = eventRepository.findById(id);
        validateEvent(event);
        event.removeParticipant(user);
        eventRepository.save(event);
    }

    void acceptUser(String token, String id, String userName){
        String owner = getLoginFromToken(token);
        validateInput(id);
        MusicEvent event = eventRepository.findById(id);
        validateEvent(event);
        validatePrivileges(owner, event);
        if(!event.getPendingParticipants().contains(userName)){
            throw new EventException("No such user waits for approval", NOT_FOUND);
        }
        event.acceptUser(userName);
        eventRepository.save(event);
    }

    void rejectUser(String token, String id, String userName){
        String owner = getLoginFromToken(token);
        validateInput(id);
        MusicEvent event = eventRepository.findById(id);
        validateEvent(event);
        validatePrivileges(owner, event);
        if(!event.getPendingParticipants().contains(userName)){
            throw new EventException("No such user waits for approval", NOT_FOUND);
        }
        event.rejectUser(userName);
        eventRepository.save(event);
    }

    List<String> getPendingUsers(String token, String id){
        String owner = getLoginFromToken(token);
        validateInput(id);
        MusicEvent event = eventRepository.findById(id);
        validateEvent(event);
        validatePrivileges(owner, event);
        return event.getPendingParticipants();
    }

    List<String> getListOfEventsUserPending(String token){
        String userName = getLoginFromToken(token);
        return eventRepository.findByPendingParticipantsIn(userName)
                .stream()
                .map( MusicEvent::getId).collect(toList());
    }

    private void checkIfJoinedBefore(MusicEvent event, String userName){
        if(event.getParticipants().contains(userName)){
            throw new EventException("Usser has already joined this course", CONFLICT);
        }
        if (event.getPendingParticipants().contains(userName)){
            throw new EventException("User is waiting for owner's approval", CONFLICT);
        }
    }

    private void checkIfOwnEvent(MusicEvent event, String userName) {
        if(event.getOwner().equals(userName)) {
            throw new EventException("You can't join your own event!", CONFLICT);
        }
    }
}

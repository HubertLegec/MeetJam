package com.pik.event.participants;

import com.pik.event.BaseEventService;
import com.pik.event.EventRepository;
import com.pik.event.MusicEvent;
import com.pik.event.exception.EventException;
import com.pik.security.TokenHandler;

import java.util.List;

import static com.pik.event.exception.EventsError.*;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;


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
            throw new EventException(NO_SUCH_USER, NOT_FOUND);
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
            throw new EventException(NO_SUCH_USER, NOT_FOUND);
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
            throw new EventException(USER_ALREADY_JOINED, CONFLICT);
        }
        if (event.getPendingParticipants().contains(userName)){
            throw new EventException(USER_IS_WAITING_FOR_APPROVAL, CONFLICT);
        }
    }

    private void checkIfOwnEvent(MusicEvent event, String userName) {
        if(event.getOwner().equals(userName)) {
            throw new EventException(CAN_NOT_JOIN_OWN_EVENT, CONFLICT);
        }
    }
}

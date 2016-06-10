package com.pik.event.participants;

import com.pik.event.EventException;
import com.pik.event.EventNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/event")
public class EventParticipantsController {

    private EventParticipantsService eventParticipantsService;

    @Autowired
    public EventParticipantsController(EventParticipantsService eventParticipantsService) {
        this.eventParticipantsService = eventParticipantsService;
    }

    @RequestMapping(value = "join", method = GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<String> joinEvent(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestParam String id) {
        eventParticipantsService.joinEvent(token, id);
        return ResponseEntity.ok("SUCCESS");
    }

    @RequestMapping(value = "leave", method = GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<String> leaveEvent(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestParam String id) {
        eventParticipantsService.leaveEvent(token, id);
        return ResponseEntity.ok("SUCCESS");
    }

    @RequestMapping(value = "acceptUser", method = GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<String> acceptUser(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestParam String id, @RequestParam String user) {
        eventParticipantsService.acceptUser(token, id, user);
        return ResponseEntity.ok("SUCCESS");
    }

    @RequestMapping(value = "rejectUser", method = GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<String> rejectUser(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestParam String id, @RequestParam String user) {
        eventParticipantsService.rejectUser(token, id, user);
        return ResponseEntity.ok("SUCCESS");
    }

    @RequestMapping(value = "pendingUsers", method = GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<String>> getPendingUsers(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestParam String id) {
        List<String> result = eventParticipantsService.getPendingUsers(token, id);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "eventsUserPending", method = GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<String>> getEventsUserPending(@RequestHeader(value = "X-AUTH-TOKEN") String token) {
        List<String> result = eventParticipantsService.getListOfEventsUserPending(token);
        return ResponseEntity.ok(result);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Event not found")
    @ExceptionHandler(EventNotFoundException.class)
    public void statusForNoSuchEvent() {
    }

    @ExceptionHandler(EventException.class)
    public ResponseEntity<String> eventExceptionHandler(EventException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
}

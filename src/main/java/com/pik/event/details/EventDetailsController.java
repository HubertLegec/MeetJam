package com.pik.event.details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/event")
public class EventDetailsController {
    private EventDetailsService eventDetailsService;

    @Autowired
    public EventDetailsController(EventDetailsService eventDetailsService) {
        this.eventDetailsService = eventDetailsService;
    }

    @RequestMapping(value = "details", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDetailsDTO> getEventDetails(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestParam String id) {
        EventDetailsDTO response = eventDetailsService.getEventDetails(id);
        return ResponseEntity.status(OK).body(response);
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND,
            reason="Event not found")
    @ExceptionHandler(EventNotFoundException.class)
    public void statusForNoSuchEvent() {}
}

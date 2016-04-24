package com.pik.event;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/api/event")
public class EventController {
    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(value = "list", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<EventDTO>> eventListByCityAndDate(@RequestParam(required = false) String city,
                                                                 @RequestParam(required = false) String instrument,
                                                                 @RequestParam(required = false) String dateFrom,
                                                                 @RequestParam(required = false) String dateTo){
        List<EventDTO> result = eventService.fetchEventsByCityAndInstrumentAndDate(city, instrument, dateFrom, dateTo);
        return ResponseEntity
                .status(getResponseStatus(result))
                .body(result);
    }

    @RequestMapping(value = "myList", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<EventDTO>> eventListByOwnerAndDate(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                  @RequestParam String dateFrom,
                                                                  @RequestParam String dateTo){
        List<EventDTO> result = eventService.fetchEventListByOwner(token, dateFrom, dateTo);
        return ResponseEntity
                .status(getResponseStatus(result))
                .body(result);
    }

    @RequestMapping(value = "joinedList", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<EventDTO>> eventListUserJoined(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                              @RequestParam String dateFrom,
                                                              @RequestParam String dateTo){
        List<EventDTO> result = eventService.fetchJoinedEventList(token, dateFrom, dateTo);
        return ResponseEntity
                .status(getResponseStatus(result))
                .body(result);
    }


    private HttpStatus getResponseStatus(List<EventDTO> result){
        if(result.size() > 0){
            return HttpStatus.OK;
        } else{
            return HttpStatus.NOT_FOUND;
        }
    }
}

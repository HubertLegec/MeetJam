package com.pik.event;

import com.pik.common.InstrumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


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
                                                                 @RequestParam(required = false) String dateTo) {
        List<EventDTO> result = eventService.fetchEventsByCityAndInstrumentAndDate(city, instrument, dateFrom, dateTo);
        return ResponseEntity
                .status(getResponseStatus(result))
                .body(result);
    }

    @RequestMapping(value = "myList", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<EventDTO>> eventListByOwnerAndDate(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                  @RequestParam(required = false) String dateFrom,
                                                                  @RequestParam(required = false) String dateTo) {
        List<EventDTO> result = eventService.fetchEventListByOwner(token, dateFrom, dateTo);
        return ResponseEntity
                .status(getResponseStatus(result))
                .body(result);
    }

    @RequestMapping(value = "joinedList", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<EventDTO>> eventListUserJoined(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                              @RequestParam(required = false) String dateFrom,
                                                              @RequestParam(required = false) String dateTo) {
        List<EventDTO> result = eventService.fetchJoinedEventList(token, dateFrom, dateTo);
        return ResponseEntity
                .status(getResponseStatus(result))
                .body(result);
    }

    @RequestMapping(value = "availableInstruments", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<String>> getAvailableInstruments() {
        List<String> instrumentNames = Arrays.asList(InstrumentType.values())
                .stream()
                .map(InstrumentType::getName).collect(toList());
        return ResponseEntity.ok(instrumentNames);
    }

    @RequestMapping(value = "create", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateEventResultDTO> createEvent(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                            @RequestParam String title,
                                                            @RequestParam String city,
                                                            @RequestParam String date) {
        CreateEventResultDTO resultDTO = eventService.createEvent(token, title, city, date);
        if (resultDTO.getId() != null) {
            return ResponseEntity.status(CREATED).body(resultDTO);
        } else {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(resultDTO);
        }
    }

    @RequestMapping(value = "delete", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteEvent(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestParam String id) {
        try {
            eventService.removeEvent(token, id);
            return ResponseEntity.ok("REMOVED");
        } catch (RemoveEventException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    private HttpStatus getResponseStatus(List<EventDTO> result) {
        if (result.size() > 0) {
            return OK;
        } else {
            return NOT_FOUND;
        }
    }
}

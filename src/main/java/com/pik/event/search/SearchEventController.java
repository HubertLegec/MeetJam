package com.pik.event.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/api/event")
public class SearchEventController {
    private SearchEventService searchEventService;

    @Autowired
    public SearchEventController(SearchEventService searchEventService) {
        this.searchEventService = searchEventService;
    }

    @RequestMapping(value = "list", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<EventDTO>> eventListByCityAndDate(SearchEventParamsDTO searchParams) {
        List<EventDTO> result = searchEventService.fetchEventsByCityAndInstrumentAndDate(searchParams);
        return ResponseEntity
                .status(getResponseStatus(result))
                .body(result);
    }

    @RequestMapping(value = "myList", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<EventDTO>> eventListByOwnerAndDate(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                  DateRangeDTO dateRange) {
        List<EventDTO> result = searchEventService.fetchEventListByOwner(token, dateRange);
        return ResponseEntity
                .status(getResponseStatus(result))
                .body(result);
    }

    @RequestMapping(value = "joinedList", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<EventDTO>> eventListUserJoined(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                              DateRangeDTO dateRange) {
        List<EventDTO> result = searchEventService.fetchJoinedEventList(token, dateRange);
        return ResponseEntity
                .status(getResponseStatus(result))
                .body(result);
    }

    private HttpStatus getResponseStatus(List<EventDTO> result) {
        if (result.size() > 0) {
            return OK;
        } else {
            return NOT_FOUND;
        }
    }
}

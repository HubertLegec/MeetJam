package com.pik.event;

import com.pik.event.dto.CreateEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/event")
public class CreateRemoveEventController {
    private CreateRemoveEventService createRemoveEventService;

    @Autowired
    public CreateRemoveEventController(CreateRemoveEventService createRemoveEventService) {
        this.createRemoveEventService = createRemoveEventService;
    }

    @RequestMapping(value = "create", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateEventResultDTO> createEvent(@RequestHeader(value = "X-AUTH-TOKEN") String token, CreateEventDTO input) {
        CreateEventResultDTO resultDTO = createRemoveEventService.createEvent(token, input);
        if (resultDTO.getId() != null) {
            return ResponseEntity.status(CREATED).body(resultDTO);
        } else {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(resultDTO);
        }
    }

    @RequestMapping(value = "delete", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteEvent(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestParam String id) {
        try {
            createRemoveEventService.removeEvent(token, id);
            return ResponseEntity.ok("REMOVED");
        } catch (RemoveEventException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}

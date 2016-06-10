package com.pik.utils;

import com.pik.common.InstrumentType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("api/utils")
public class UtilsController {
    @RequestMapping(value = "availableInstruments", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<String>> getAvailableInstruments() {
        List<String> instrumentNames = Arrays.asList(InstrumentType.values())
                .stream()
                .map(InstrumentType::getName).collect(toList());
        return ResponseEntity.ok(instrumentNames);
    }
}

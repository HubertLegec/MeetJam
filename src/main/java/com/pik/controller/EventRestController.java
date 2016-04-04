package com.pik.controller;

import com.pik.model.MusicEvent;
import com.pik.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Hubert on 01.04.2016.
 */
@RestController
@RequestMapping("/api/events")
public class EventRestController{

    @Autowired
    private EventRepository repo;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<MusicEvent>> getAll(){
        return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }
}

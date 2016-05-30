package com.pik.event.details;

import com.pik.common.InstrumentType;
import com.pik.event.BaseEventService;
import com.pik.event.EventNotFoundException;
import com.pik.event.EventRepository;
import com.pik.event.MusicEvent;
import com.pik.security.TokenHandler;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class EventDetailsService extends BaseEventService {


    public EventDetailsService(EventRepository eventRepository, TokenHandler tokenHandler) {
        this.eventRepository = eventRepository;
        this.tokenHandler = tokenHandler;
    }

    public EventDetailsDTO getEventDetails(String id)
    {
        MusicEvent event = eventRepository.findById(id);
        if (event == null){
            throw new EventNotFoundException();
        }
        return new EventDetailsDTO(event.getDescription(),
                event.getInstrumentsNeeded().stream().map(InstrumentType::getName).collect(toList()),
                event.getParticipants());
    }

    public void updateEventDetails(String token, UpdateDetailsDTO input) {
        String owner = getLoginFromToken(token);
        validateInput(input.getId());
        MusicEvent eventToUpdate = eventRepository.findById(input.getId());
        validateEvent(eventToUpdate);
        validatePrivileges(owner, eventToUpdate);
        eventToUpdate.setDescription(input.getDescription());
        if(input.getInstrumentsNeeded() != null){
            List<InstrumentType> instruments = new ArrayList<>();
            input.getInstrumentsNeeded()
                    .forEach( instrumentName ->
                            instruments.add(InstrumentType.fromString(instrumentName))
                    );
            eventToUpdate.setInstrumentsNeeded(instruments);
        }
        eventRepository.save(eventToUpdate);
    }
}

package com.pik.event.details;

import com.pik.common.InstrumentType;
import com.pik.event.EventRepository;
import com.pik.event.MusicEvent;

import static java.util.stream.Collectors.toList;

public class EventDetailsService {
    private EventRepository eventRepository;

    public EventDetailsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
}

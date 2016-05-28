package com.pik.event.search;

import com.pik.common.InstrumentType;
import com.pik.event.EventRepository;
import com.pik.event.MusicEvent;
import com.pik.event.dto.DateRangeDTO;
import com.pik.event.dto.EventDTO;
import com.pik.event.dto.SearchEventParamsDTO;
import com.pik.security.TokenHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SearchEventService {
    private EventRepository eventRepository;
    private TokenHandler tokenHandler;

    public SearchEventService(EventRepository eventRepository, TokenHandler tokenHandler) {
        this.eventRepository = eventRepository;
        this.tokenHandler = tokenHandler;
    }

    public List<EventDTO> fetchEventsByCityAndInstrumentAndDate(SearchEventParamsDTO searchParams) {
        InstrumentType instrumentNeeded = InstrumentType.fromString(searchParams.getInstrument());
        List<MusicEvent> eventsByCity = eventRepository
                .findByCityAndInstrumentNeededAndDateBetween(searchParams.getCity(), instrumentNeeded, searchParams.getDateFrom(), searchParams.getDateTo());
        return transformToEventDTOList(eventsByCity);
    }

    public List<EventDTO> fetchEventListByOwner(String token, DateRangeDTO dateRange) {
        String owner = getLoginFromToken(token);
        if (owner == null) {
            return new ArrayList<>();
        }
        List<MusicEvent> ownerEvents = eventRepository
                .findByOwnerAndDateBetween(owner, dateRange.getDateFrom(), dateRange.getDateTo());
        return transformToEventDTOList(ownerEvents);
    }

    public List<EventDTO> fetchJoinedEventList(String token, DateRangeDTO dateRange) {
        String participant = getLoginFromToken(token);
        if (participant == null) {
            return new ArrayList<>();
        }
        List<MusicEvent> userEvents = eventRepository
                .findByParticipantsInAndDateBetween(participant, dateRange.getDateFrom(), dateRange.getDateTo());
        return transformToEventDTOList(userEvents);
    }

    private String getLoginFromToken(String token) {
        try {
            return tokenHandler.parseUserFromToken(token)
                    .getUsername();
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }

    private List<EventDTO> transformToEventDTOList(List<MusicEvent> events) {
        return events
                .stream()
                .map(EventDTO::new)
                .collect(toList());
    }
}

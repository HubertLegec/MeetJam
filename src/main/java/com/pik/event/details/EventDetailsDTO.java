package com.pik.event.details;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsDTO {
    private String description;
    private List<String> instrumentsNeeded = new ArrayList<>();
    private List<String> participants = new ArrayList<>();

    public EventDetailsDTO(){}

    public EventDetailsDTO(String description, List<String> instrumentsNeeded, List<String> participants) {
        this.description = description;
        this.instrumentsNeeded = instrumentsNeeded;
        this.participants = participants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getInstrumentsNeeded() {
        return instrumentsNeeded;
    }

    public void setInstrumentsNeeded(List<String> instrumentsNeeded) {
        this.instrumentsNeeded = instrumentsNeeded;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}

package com.pik.event.details;

import java.util.List;

public class UpdateDetailsDTO {
    private String id;
    private String description;
    private List<String> instrumentsNeeded;

    public UpdateDetailsDTO() {

    }

    public UpdateDetailsDTO(String id, String description, List<String> instrumentsNeeded) {
        this.id = id;
        this.description = description;
        this.instrumentsNeeded = instrumentsNeeded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

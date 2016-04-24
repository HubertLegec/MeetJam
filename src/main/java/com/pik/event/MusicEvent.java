package com.pik.event;

import com.pik.common.InstrumentType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document
public class MusicEvent {
    @Id
    private BigInteger id;
    private String owner;
    private String title;
    private String description;
    private String city;
    private LocalDateTime date;
    private List<String> participants = new ArrayList<>();
    private List<InstrumentType> instrumentsNeeded = new ArrayList<>();


    public MusicEvent(LocalDateTime date, String city, String title, String owner) {
        this.date = date;
        this.city = city;
        this.title = title;
        this.owner = owner;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addParticipant(String login){
        if(!participants.contains(login)) {
            participants.add(login);
        }
    }

    public void addNeededInstrument(InstrumentType instrument){
        if(!instrumentsNeeded.contains(instrument)){
            instrumentsNeeded.add(instrument);
        }
    }

    public List<String> getParticipants(){
        return participants;
    }
}
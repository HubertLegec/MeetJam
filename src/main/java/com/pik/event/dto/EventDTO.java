package com.pik.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pik.event.MusicEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDTO {
    private String id;
    private String owner;
    private String title;
    private String city;
    private String date; //LocalDateTime formatted as ISO_DATE_TIME

    public EventDTO() {

    }

    public EventDTO(MusicEvent event){
        this.id = event.getId();
        this.owner = event.getOwner();
        this.title = event.getTitle();
        this.city = event.getCity();
        this.date = event.getDate().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }

    @JsonIgnore
    public LocalDateTime getConvertedDate(){
        return LocalDateTime.parse(date);
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "EventDTO{" +
                "owner='" + owner + '\'' +
                ", title='" + title + '\'' +
                ", city='" + city + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

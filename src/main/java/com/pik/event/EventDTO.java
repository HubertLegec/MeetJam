package com.pik.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDTO {
    private String owner;
    private String title;
    private String city;
    private String date; //LocalDateTime formatted as ISO_DATE_TIME

    public EventDTO() {
    }

    public EventDTO(String owner, String title, String city, LocalDateTime date) {
        this.owner = owner;
        this.title = title;
        this.city = city;
        this.date = date.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public EventDTO(MusicEvent event){
        this.owner = event.getOwner();
        this.title = event.getTitle();
        this.city = event.getCity();
        this.date = event.getDate().format(DateTimeFormatter.ISO_DATE_TIME);
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

package com.pik.event.createremove;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CreateEventDTO {
    private String title;
    private String city;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    public CreateEventDTO() {

    }

    public CreateEventDTO(String city, LocalDateTime date, String title) {
        this.city = city;
        this.date = date;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

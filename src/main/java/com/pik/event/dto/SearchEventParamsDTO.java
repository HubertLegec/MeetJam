package com.pik.event.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class SearchEventParamsDTO {
    private String city;
    private String instrument;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTo;

    public SearchEventParamsDTO() {

    }

    public SearchEventParamsDTO(String city, LocalDateTime dateFrom, LocalDateTime dateTo, String instrument) {
        this.city = city;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.instrument = instrument;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }
}

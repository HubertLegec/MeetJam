package com.pik.event;

public enum EventsError {
    INCORRECT_DATE("Incorrect date"),
    EMPTY_CITY_FIELD("City must not be empty"),
    EMPTY_TITLE_FILED("Title must not be empty"),
    USERNAME_ERROR("Can't recognize user"),
    EVENT_ID_ERROR("Event id must not be empty"),
    EVENT_DOES_NOT_EXIST("Event does not exist"),
    NOT_USERS_EVENT("This is not your event");

    private String message;

    EventsError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

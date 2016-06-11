package com.pik.event.exception;

public enum EventsError {
    INCORRECT_DATE("Incorrect date"),
    EMPTY_CITY_FIELD("City must not be empty"),
    EMPTY_TITLE_FILED("Title must not be empty"),
    USERNAME_ERROR("Can't recognize user"),
    EVENT_ID_ERROR("Event id must not be empty"),
    NOT_USERS_EVENT("This is not your event"),
    NO_SUCH_USER("No such user waits for approval"),
    USER_IS_WAITING_FOR_APPROVAL("User is waiting for owner's approval"),
    USER_ALREADY_JOINED("Usser has already joined this course"),
    CAN_NOT_JOIN_OWN_EVENT("You can't join your own event!");

    private String message;

    EventsError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

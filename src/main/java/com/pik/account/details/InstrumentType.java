package com.pik.account.details;

public enum InstrumentType {
    GUITAR("Guitar"),
    PIANO("Piano"),
    KEYBOARD("Keyboard");

    private String name;

    InstrumentType(String name){
        this.name = name;
    }
}

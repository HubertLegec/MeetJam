package com.pik.common;

public enum InstrumentType {
    GUITAR("Guitar"),
    PIANO("Piano"),
    KEYBOARD("Keyboard"),
    DRUMS("Drums"),
    BASS_GUITAR("Bass guitar");

    private String name;

    InstrumentType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static InstrumentType fromString(String name) {
        if (name != null) {
            for (InstrumentType i : InstrumentType.values()) {
                if (name.equalsIgnoreCase(i.name)) {
                    return i;
                }
            }
        }
        return null;
    }
}

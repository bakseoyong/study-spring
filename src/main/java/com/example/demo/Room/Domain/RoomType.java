package com.example.demo.Room.Domain;

public enum RoomType {
    숙박("숙박"),
    DayUse("DayUse");

    private String value;

    RoomType(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}

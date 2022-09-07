package com.example.demo.Point.Domain;

public enum PointStatus {
    적립("collect"),
    사용("use"),
    소멸("expired");
    private String value;
    PointStatus(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}

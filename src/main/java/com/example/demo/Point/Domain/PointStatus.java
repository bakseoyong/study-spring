package com.example.demo.Point.Domain;

public enum PointStatus {
    지급예정("scheduled"),
    취소("cancel"),
    적립("earn"),
    사용("use"),
    소멸("expired"),
    오류("error");
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

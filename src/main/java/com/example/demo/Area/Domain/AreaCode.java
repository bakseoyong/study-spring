package com.example.demo.Area.Domain;

public enum AreaCode {
    미정("a-100000"),
    광주서구("a-100062"),
    광주동구남구("a-100063"),
    광주첨단("a-100064"),
    광주하남("a-100065"),
    광주북구("a-100066");

    private String value;

    AreaCode(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}

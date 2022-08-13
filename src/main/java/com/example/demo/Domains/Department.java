package com.example.demo.Domains;

public enum Department {
    컴퓨터공학과("cs"),
    정보통신공학과("ice"),
    전자공학과("ee");
    
    private String value;

    Department(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}

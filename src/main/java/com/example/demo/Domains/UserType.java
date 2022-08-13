package com.example.demo.Domains;

public enum UserType {
    VISITOR("visitor"),
    STUDENT("student"),
    ADVERTISER("advertiser"),
    ADMIN("admin");
    
    private String value;
    
    UserType(String value){
        this.value = value;
    }
    
    public String getKey(){
        return this.name();
    }

    public String getValue(){
        return value;
    }
}

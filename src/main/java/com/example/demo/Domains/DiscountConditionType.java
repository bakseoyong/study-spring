package com.example.demo.Domains;

public enum DiscountConditionType {
    요일("specific_day"),
    이상("eqaul_more_than"),
    기간내체크인("check_in_period");

    private String value;

    DiscountConditionType(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }

    }

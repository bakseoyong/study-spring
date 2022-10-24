package com.example.demo.Image.Domain;

import java.util.List;

public enum ImageType {
    //width x height
    Thumbnail("300x300"),
    UserProfile("50x50"),
    Review("100x100");

    private String value;

    ImageType(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public int[] getValue(){
        String[] splits = value.split("x");
        int[] ints = new int[2];
        ints[0] = Integer.parseInt(splits[0]);
        ints[1] = Integer.parseInt(splits[1]);
        return ints;
    }
}

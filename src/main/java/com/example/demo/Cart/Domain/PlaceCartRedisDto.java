package com.example.demo.Cart.Domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PlaceCartRedisDto implements Cartable{
    private String type;
    private Long roomId;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;

    @Override
    public String toSession(){
        return "#" + "type:" + type + ",roomId:" + roomId +
                ",checkinDate:" + checkinDate + ",checkoutDate:" + checkoutDate;
    }

    @Override
    public String getType(){
        return type;
    }

    public static PlaceCartRedisDto toDto(String string){
        System.out.println("place cart dto called!");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String[] attributes = string.split(",");

        Map<String, Object> placeMap = new HashMap<>();

        for(String attribute: attributes){
            String[] map = attribute.split(":");
            String key = map[0];
            String value = map[1];

            placeMap.put(key, value);
        }

        return mapper.convertValue(placeMap, PlaceCartRedisDto.class);
    }
}

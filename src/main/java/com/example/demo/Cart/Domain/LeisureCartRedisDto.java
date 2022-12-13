package com.example.demo.Cart.Domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class LeisureCartRedisDto implements Cartable{
    private String type; //Redis에서 타입구분을 위해
    private Long leisureId;
    private Long ticketId;
    private Long quantity;
    @Override
    public String toSession(){
        return "#" + "type:" + type + ",leisureId:" + leisureId + ",ticketId:" + ticketId + ",quantity:" + quantity;
    }

    @Override
    public String getType(){
        return type;
    }

    public static LeisureCartRedisDto toDto(String string){
        System.out.println("leisure cart dto called!");
        ObjectMapper mapper = new ObjectMapper();

        String[] attributes = string.split(",");

        Map<String, Object> leisureMap = new HashMap<>();

        for(String attribute: attributes){
//            System.out.println(attribute);
            String[] map = attribute.split(":");
            String key = map[0];
            String value = map[1];

            leisureMap.put(key, value);
        }

        return mapper.convertValue(leisureMap, LeisureCartRedisDto.class);
    }

}

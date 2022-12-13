package com.example.demo.Cart.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TransportationCartRedisDto implements Cartable{
    private String type;
    private String specific;
    private Boolean isRound;
    private String dptCode;
    private String arrCode;
    @JsonFormat(pattern = "HH.mm.ss")
    private LocalTime dptTime;
    @JsonFormat(pattern = "HH.mm.ss")
    private LocalTime arrTime;
    private LocalDate date;
    private String gradeOrNo;
    private String seat;
    private Long adultCharge;
    private Long personNum;

    public void toTrainCartItem(){
//        HashMap<String, String> cartItem = new HashMap<String, String>();
//        cartItem.put("type", "train");
//        cartItem.put()
    }

    public void toBusCartItem(){

    }

    @Override
    public String toSession(){
//        return "#" + "type:" + type + ",personNum:" + personNum + "," ...
        return "#" + "type:" + type + ",specific:" + specific + ",dptCode:" + dptCode + ",arrCode:" + arrCode +
                ",dptTime:" + dptTime + ",arrTime:" + arrTime + ",date:" + date +
                ",gradeOrNo" + gradeOrNo + ",seat:" + seat + ",adultCharge:" + adultCharge + ",personNum:" + personNum;
    }

    @Override
    public String getType(){
        return type;
    }

    public static TransportationCartRedisDto toDto(String string){
        System.out.println("transportation cart dto called!");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String[] attributes = string.split(",");

        Map<String, Object> transportationMap = new HashMap<>();

        for(String attribute: attributes){
//            System.out.println(attribute);
            String[] map = attribute.split(":");
            String key = map[0];
            String value = map[1];

            transportationMap.put(key, value);
        }

        return mapper.convertValue(transportationMap, TransportationCartRedisDto.class);
    }
}

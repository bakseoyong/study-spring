package com.example.demo.utils.Converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class AreaLatLngConverter implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = "#";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute.stream().collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .collect(Collectors.toList());
    }

//    public List<String> convertToLatLng(List<String> attributes){
//        //return Arrays.stream(attribute.split(","))
//          //      .collect(Collectors.toList());
//
//    }
}

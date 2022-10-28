package com.example.demo.utils.Converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ImagePathConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute.stream().collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .collect(Collectors.toList());
    }

    public List<String> convertToSubfolderPath(List<String> absolutePaths){
//        하위폴더만 따로 뗴어내는 법
//        => 기존에 절대경로에서 추가할때 /하위폴더/이미지명 으로 설정
//        => 반대로 뒤에있는 / 두개를 이용해서 하위폴더와 이미지명을 알아오자.
        List<String> result = new ArrayList<>();

        for(String absolutePath: absolutePaths){
            String[] routes = absolutePath.split("/");
            result.add(routes[routes.length-2] + "/" + routes[routes.length-1]);
        }

        return result;
    }
}

package com.example.demo.User.Domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Name {
    private String name;

    public Name(String name){
        if(Pattern.matches("^[가-힣]", name)){ // 내국인
            if(name.length() < 17) //한국에서 가장 긴 성 11자, 이름은 5자가 최대
                this.name = name;
            else
                throw new IllegalArgumentException("입력할 수 있는 최대 글자 수 초과");
        }else if(Pattern.matches("^[a-zA-Z]", name)){ //외국인
            this.name = name;
        }

        throw new IllegalArgumentException("여러 문자를 함께 입력할 수 없음");
    }

    public static Name of(String name){
        return new Name(name);
    }
}

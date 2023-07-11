package com.example.demo.User.Domain;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.regex.Pattern;
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phone {
    private String phone;

    private Phone(String phone) {
        if(Pattern.matches("^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", phone))
            this.phone = phone;
        else
            throw new IllegalArgumentException("잘못된 전화번호 형식");
    }

    public static Phone of(String phone){
        return new Phone(phone);
    }
}

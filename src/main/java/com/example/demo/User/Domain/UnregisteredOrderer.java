package com.example.demo.User.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("NonConsumer_Type")
public class UnregisteredOrderer extends Consumer{
    @Builder
    private UnregisteredOrderer(String id, String password, String email,
                                Name name, Phone phone){
        super(id, password, email, null, name, phone);
    }

    public static UnregisteredOrderer of(Name name, Phone phone){
        String id = UUID.randomUUID().toString();

        byte[] bytes = new byte[8];
        new Random().nextBytes(bytes);
        String password = new String(bytes, Charset.forName("UTF-8"));

        String email = id.substring(0, 8) + "@yanolja.com";

        return UnregisteredOrderer.builder()
                .id(id)
                .password(password)
                .email(email)
                .build();
    }
}


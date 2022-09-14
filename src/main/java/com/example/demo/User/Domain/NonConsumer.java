package com.example.demo.User.Domain;

import com.example.demo.Reservation.Domain.Reservation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("NonConsumer_Type")
public class NonConsumer extends User{
    @Builder
    NonConsumer (String id, String password, String email){
        super(id, password, email);
    }

    public static NonConsumer toEntity(){
        String id = UUID.randomUUID().toString();

        byte[] bytes = new byte[8];
        new Random().nextBytes(bytes);
        String password = new String(bytes, Charset.forName("UTF-8"));

        String email = id.substring(0, 8) + "@yanolja.com";

        return NonConsumer.builder()
                .id(id)
                .password(password)
                .email(email)
                .build();
    }
}


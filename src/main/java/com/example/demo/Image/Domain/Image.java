package com.example.demo.Image.Domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String domain;

    //Custom Identifier Generator로 인해 추후 타입이 변경될 가능성이 높다.
    private Long domainId;

    private String name;

    private String path;


}

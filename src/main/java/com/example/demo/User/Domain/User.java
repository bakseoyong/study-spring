package com.example.demo.User.Domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity //Table 어노테이션에 포함되는줄 알았지만 JPA 관련 코드 작동시 에러 발생
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@Getter
@Table(name="Users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name= "uuid2", strategy = "uuid2")
//    @Type(type = "uuid-char")

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    public User(String loginId, String password, String email){
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }
}

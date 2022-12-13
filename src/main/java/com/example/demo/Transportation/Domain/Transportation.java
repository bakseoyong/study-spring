package com.example.demo.Transportation.Domain;

import com.example.demo.Cart.Domain.Cartable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
abstract class Transportation{
    //버스 - 편도or왕복 , 브랜드이미지파일,  출발지, 도착지, 출발시간, 도착시간, 날짜, 우등or일반or프리미엄, 좌석(11번좌석), 성인요금, 인원수
    //철도 - 편도or왕복, 브랜드이미지파일, 출발지, 도착지, 출발시간, 도착시간, 날짜, 기차번호, 임의배정or좌석(7호차14A), 성인요금, 인원수

    private Boolean isRound;
    private String brandImgPath;
    private String dptName;
    private String arrName;
    private LocalTime dptTime;
    private LocalTime arrTIme;
    private LocalDate date;
    private String gradeOrNo;
    private String seat;
    private Long adultCharge;
    private Long personNum;
}

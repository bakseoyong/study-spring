package com.example.demo;

import com.example.demo.Area.Domain.AreaCode;
import com.example.demo.Area.Domain.Area;
import com.example.demo.Area.Domain.AreaGroups;
import com.example.demo.Area.Repository.AreaLatLngRepository;
import com.example.demo.utils.Converter.AreaLatLngConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AreaTest {
    @Autowired
    private AreaLatLngRepository areaLatLngRepository;

    public AreaLatLngConverter areaLatLngConverter = new AreaLatLngConverter();

    @BeforeEach
    public void SetUp(){
        List<String> seogu = new ArrayList<>();
        List<String> donggu = new ArrayList<>();
        List<String> chumdan = new ArrayList<>();
        List<String> hanam = new ArrayList<>();
        List<String> bukgu = new ArrayList<>();


        String ll1 = "35.1481541,126.855130"; //상무지구
        seogu.add(ll1);
        String ll2 = "35.1351498,126.857117"; //금호지구
        seogu.add(ll2);
        String ll3 = "35.1608009,126.880577"; //유스퀘어
        seogu.add(ll3);
        String ll4 = "35.1520485,126.889902"; //서구
        seogu.add(ll4);

        String ll5 = "35.1484359,126.914544"; //충장로
        donggu.add(ll5);
        String ll6 = "35.1542204,126.917846"; //대인시장
        donggu.add(ll6);
        //String ll7 = ""; //국립아시아문화전당
        String ll8 = "35.1460981,126.923128"; //동구
        donggu.add(ll8);
        String ll9 = "35.1331033,126.901986"; //남구
        donggu.add(ll9);

        String ll10 = "35.2158220,126.843192"; //첨단지구
        chumdan.add(ll10);
        String ll11 = "35.2020125,126.873263"; //양산동
        chumdan.add(ll11);

        String ll12 = "35.1730663,126.797608"; //하남
        hanam.add(ll12);
        String ll13 = "35.1636740,126.797477"; //광주여대
        hanam.add(ll13);
        String ll14 = "35.1369645,126.790872"; //송정역
        hanam.add(ll14);
        //String ll15 = ""; //광산구

        String ll16 = "35.1643546,126.909269"; //광주역
        bukgu.add(ll16);
        String ll17 = "35.1682310,126.888365"; //기아챔피언스필드
        bukgu.add(ll17);
        String ll18 = "35.1739476,126.912684"; //전대사거리
        bukgu.add(ll18);
        //String ll19 = ""; //북구

        Area bukguArea = Area.builder()
                .areaCode(AreaCode.광주북구)
                .latLngs(areaLatLngConverter.convertToDatabaseColumn(bukgu))
                .build();

        Area seoguArea = Area.builder()
                .areaCode(AreaCode.광주서구)
                .latLngs(areaLatLngConverter.convertToDatabaseColumn(seogu))
                .build();

        Area chumdanArea = Area.builder()
                .areaCode(AreaCode.광주첨단)
                .latLngs(areaLatLngConverter.convertToDatabaseColumn(chumdan))
                .build();

        Area hanamArea = Area.builder()
                .areaCode(AreaCode.광주하남)
                .latLngs(areaLatLngConverter.convertToDatabaseColumn(hanam))
                .build();

        Area dongguArea = Area.builder()
                .areaCode(AreaCode.광주동구남구)
                .latLngs(areaLatLngConverter.convertToDatabaseColumn(donggu))
                .build();


        areaLatLngRepository.save(bukguArea);
        areaLatLngRepository.save(seoguArea);
        areaLatLngRepository.save(chumdanArea);
        areaLatLngRepository.save(hanamArea);
        areaLatLngRepository.save(dongguArea);
    }

    @Test
    public void 장소_어느_구역에_속하는지_테스트(){
//        Location location = Location.builder()
//                .lat("35.176196")
//                .lng("126.925427")
//                .build();
        Double shortest = 99999.0;
        AreaCode areaCode = AreaCode.미정;

        //무등도서관
        Double placeLat = 35.176196;
        Double placeLng = 126.925427;

        //광주 5개 지역
        List<Area> areas = areaLatLngRepository.findAll();
        AreaGroups areaGroups = new AreaGroups(areas);
        areaGroups.decideAreaCode(placeLat, placeLng);

        System.out.println(areaCode.getKey() + areaCode.getValue());
    }
}

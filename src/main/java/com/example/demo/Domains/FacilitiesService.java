package com.example.demo.Domains;

public enum FacilitiesService {
    애견동반("0"),
    스파_월풀_욕조("1"),
    주차가능("2"),
    개별바베큐("3"),
    바다전망("4"),
    수영장("5"),
    계곡인접("6"),
    풀빌라("7"),
    바베큐("8"),
    노래방("9"),
    조식운영("10"),
    키즈("11"),
    독채펜션("12"),
    주방("13"),
    PC라운지("14"),
    와이파이("15"),
    사우나("16"),
    복층구조("17"),
    픽업가능("18"),
    독채객실("19"),
    파티가능("20"),
    흡연구역("21"),
    객실금연("22"),
    식사가능("23"),
    이벤트가능("24"),
    체험("25"),
    레스토랑("26"),
    바("27"),
    연회장("28"),
    피트니스("29"),
    뷔페("30"),
    공항셔틀("31"),
    어메니티("32"),
    무중단데스크("33"),
    노트북대여("34"),
    커피숍("35"),
    비즈니스("36"),
    트윈베드("37"),
    루프탑("38"),
    커플룸("39"),
    유료세탁("40"),
    무료세탁("41"),
    공용주방("42"),
    공용거실("43"),
    대여("44"),
    한옥("45"),
    수화물보관("46"),
    VOD("47"),
    투어("48"),
    족구장("49"),
    매점_편의점("50"),
    상비약("51"),
    패밀리("52");

    private String value;

    FacilitiesService(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}

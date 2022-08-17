package com.example.demo.Domains;

public enum BusinessType {
    모텔("motel"),
    호텔("hotel"),
    펜션("pension"),
    게하_한옥("guesthouse_hanok"),
    프리미엄호텔("premium_hotel"),
    프리미엄펜션("premium_pension"),
    리조트("resort"),
    글램핑_캠핑("glamping_camping"),
    테마펜션("theme_pension"),
    일성급호텔("1_stars_hotel"),
    이성급호텔("2_stars_hotel"),
    삼성급호텔("3_stars_hotel"),
    사성급호텔("4_stars_hotel"),
    오성급호텔("5_stars_hotel");


    private String value;

    BusinessType(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}

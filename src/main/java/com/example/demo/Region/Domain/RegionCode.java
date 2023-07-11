package com.example.demo.Region.Domain;

public enum RegionCode {
    광주광역시("gwnagju"),
    영광군("jeonnam01"),
    장성군("jeonnam02"),
    담양군("jeonnam03"),
    곡성군("jeonnam04"),
    구례군("jeonnam05"),
    광양시("jeonnam06"),
    순천군("jeonnam07"),
    여수시("jeonnam08"),
    화순군("jeonnam09"),
    보성군("jeonnam10"),
    고흥군("jeonnam11"),
    장흥군("jeonnam12"),
    나주시("jeonnam13"),
    영암군("jeonnam14"),
    강진군("jeonnam15"),
    진도군("jeonnam16"),
    완도군("jeonnam17"),
    해남군("jeonnam18"),
    목포시("jeonnam19"),
    신안군("jeonnam20"),
    무안군("jeonnam21"),
    함평군("jeonnam22");

    private String value;

    RegionCode(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}

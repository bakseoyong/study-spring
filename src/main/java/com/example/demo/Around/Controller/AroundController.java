package com.example.demo.Around.Controller;

import com.example.demo.Around.Service.AroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AroundController {
    @Autowired
    private AroundService aroundService;

    @GetMapping("/around")
    public void findAroundPlaces(
            @RequestParam("lat") String lat,
            @RequestParam("lng") String lng
    ){

        aroundService.getAroundPlaces(Double.parseDouble(lat), Double.parseDouble(lng));
    }

//    @GetMapping("/around/keyword-recommend") //선착순 쿠폰 탭

//    @GetMapping("/around/keyword-motel")
//    public void findKeywordMotelAroundPlace(
//        @RequestParam(value = "lat", required = false) String lat,
//        @RequestParam(value = "lng", required = false) String lng,
//        @RequestParam(value = "")
//
//    ){
//
//    }


}

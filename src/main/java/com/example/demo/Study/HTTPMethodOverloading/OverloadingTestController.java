package com.example.demo.Study.HTTPMethodOverloading;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OverloadingTestController {
    @PostMapping(value = "/test/overloading", params = {"train"})
    public String testOverloading(@RequestParam String train){
        System.out.println("train called!!!");

        return "index";
    }

    @PostMapping(value = "/test/overloading", params = {"bus"})
    public String testOverloading2(@RequestParam String bus){
        System.out.println("bus called!!!");

        return "index";
    }
}

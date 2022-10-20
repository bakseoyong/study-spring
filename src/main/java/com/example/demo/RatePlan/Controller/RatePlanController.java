package com.example.demo.RatePlan.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RatePlanController {
    /**
     * 필요한 컨트롤러 /rateplan/create
     * /rateplan/new
     * /rateplan/update
     * /rateplan/delete
     */
    @GetMapping("/rateplan/new")
    public String newRatePlan(){
        return "newRatePlan";
    }

//    @PostMapping("/rateplan/create")
//    public String create(@RequestBody Dto dto){
//        ratePlanController.create(dto);
//        return "OK";
//    }
}

package com.example.demo.Area.Controller;

import com.example.demo.Area.Service.AreaService;
import com.example.demo.Auth.Domain.GuestAuth;
import com.example.demo.utils.Session.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * 응답 헤더에 Date Header를 추가하는 방법
 * 1. HttpServletResponse
 * 2. ResponseEntity
 * 두 가지가 있는데 RESTful 형식으로 짜본적이 없으니 한번 도전해 보자
 * => Thymeleaf를 사용하기 위해서는 @Controller를 사용해야 한다고 한다.
 * => String을 리턴 타입으로 제공하면 뷰 템플릿 엔진에서 자동으로 탐색을 해주는데
 * => RestController는 ResponseBody를 리턴 타입으로 설정해야 하기 때문이다.
 */
//@RestController
@Controller
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping(value = "/area/{areaCode}")
    public String searchPlacesInArea(Model model, @PathVariable String areaCode,
                                     @CookieValue("clientId") Cookie cookie,
                                     HttpServletResponse res, HttpSession session){
        try {
            String id = GuestAuth.getIdentifier(cookie, res);

            res.setDateHeader("Date", System.currentTimeMillis());

            Double[] clientLatLng = SessionUtils.getClientLatLng(id, session);

            //야놀자 초이스 조회 결과만 나오는건 아니지만 그거 까지 생각하면 너무 힘드므로 야놀자 초이스만 조회 결과로 나온다고 하자.
            //야놀자 초이스 결과만 나오는게 아니니까 직렬화를 이용해야 한다.
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

            LocalDate checkinDate = new java.sql.Date(format.parse(res.getHeader("Date")).getTime()).toLocalDate();
            LocalDate checkoutDate = checkinDate.plusDays(1);

                    String
            json = areaService.findByAreaCode(areaCode, clientLatLng[0], clientLatLng[1], checkinDate, checkoutDate);

            model.addAttribute("areaPlaceItems", json);

            return "areaPlaces";
        }catch (ParseException e){
            e.printStackTrace();
            return "index";
        }
    }


    @GetMapping(value = "/area/otherHeaderTest")
    public String otherHeaderTest(@RequestHeader("Accept-Encoding") String encoding){
        System.out.println(encoding);
        return "index";
    }

    //1. 헤더가 setDateHeader를 통해 잘 들어갔다.
    //2. GET 매핑때마다 null이 뜬다... postman은 헤더에 잘 들어가는지 확인해 보자
    //3. postman에서 응답 헤더에 정상적으로 출력되고, 야놀자에서도 응답 헤더에만 존재하는 걸로 확인
    //4. 모든 API마다 setHeader를 하는건 너무 중복된다.
    @GetMapping(value = "/area/setDateHeader")
    public String setDateHeader(HttpServletResponse res){
        System.out.println("setDateHeader test entered!");
        if(res.getHeader("Date") == null) {
            System.out.println("date is null");
            res.setDateHeader("Date", System.currentTimeMillis());
        }

        System.out.println("date header value is : " + res.getHeader("Date"));

        return "index";
    }
}

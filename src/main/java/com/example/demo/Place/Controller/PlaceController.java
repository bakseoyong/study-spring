package com.example.demo.Place.Controller;

import com.example.demo.Place.DTO.PlaceDto;
import com.example.demo.Location.Domain.Location;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Service.PlaceService;
import com.example.demo.Room.Dto.RoomDto;
import jdk.nashorn.internal.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    /**
     * 추가되어야 할 사항들
     * 1. 날짜를 입력받아서 해당 날짜의 방 상태에 대해 알려주기
     *
     */

    /**
     * 한 단계씩 해보자
     * 1. service를 거치지 않고 dto만 만들어서 model에 붙혀서 보내보자.
     */
    @GetMapping("/place/test1")
    public String testPlaceStep1(Model model){
        RoomDto roomDto1 = RoomDto.builder()
                .id(1L)
                .name("테스트룸1")
                .type("숙박")
                .maximumPersonNum(2L)
                .standardPersonNum(2L)
                .originalPrice(100000L)
                .discountPrice(49999L)
                .build();

        RoomDto roomDto2 = RoomDto.builder()
                .id(2L)
                .name("테스트룸2")
                .type("숙박")
                .maximumPersonNum(2L)
                .standardPersonNum(2L)
                .originalPrice(100000L)
                .discountPrice(49999L)
                .build();

        RoomDto roomDto3 = RoomDto.builder()
                .id(3L)
                .name("테스트룸3")
                .type("숙박")
                .maximumPersonNum(2L)
                .standardPersonNum(2L)
                .originalPrice(100000L)
                .discountPrice(49999L)
                .build();

        List<RoomDto> roomDtos = new ArrayList<>();
        roomDtos.add(roomDto1);
        roomDtos.add(roomDto2);
        roomDtos.add(roomDto3);

        PlaceDto placeDto = PlaceDto.builder().roomDtos(roomDtos).build();

        model.addAttribute("placeDto", placeDto);
        return "places";
    }

    //2단계 해당 날짜에 매진인지 확인하기 => 해당 날짜에 방이 몇개 남아있는지 확인하기 => Mysql로 타임 테이블
    //PlaceService에 테스트 메서드 만들어서 날짜별 남은 방 개수만 출력해줄 수 있는 서비스 만들기
    @GetMapping("/place/test2")
    public String testPlaceStep2(Model model){
        PlaceDto placeDto = placeService.getPlaceTest2Info();
        model.addAttribute("placeDto", placeDto);
        return "placesTest2";
    }

    //3단계 이미지 까지 넣어서 보여주자
    @GetMapping("/place/test3")
    public String testPlaceStep3(Model model){
        PlaceDto placeDto = placeService.getPlaceTest3Info();

        model.addAttribute("placeDto", placeDto);
        return "placesTest3";
    }

    /**
     * 호출 할 떄마다 a가 한개씩 늘어난다.
     */
    @GetMapping("/place/cookieUpdate")
    public String cookieUpdateTest(Model model, HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();

        if(cookies == null){
            Cookie cookie = new Cookie("cookieUpdateTest", "a");
            response.addCookie(cookie);
            return "index";
        }

        for(Cookie cookie: cookies){
            System.out.println("cookie name is : " + cookie.getName() + ", cookie value is : " + cookie.getValue());
            if(cookie.getName().equals("cookieUpdateTest")){
                String value = cookie.getValue();
                Cookie cookie1 = new Cookie("cookieUpdateTest", value.concat("a"));
                response.addCookie(cookie1);
                return "index";
            }
        }

        //CookieUpdateTest가 없던 경우 ( 맨처음이거나, 만료된 경우)
        Cookie cookie = new Cookie("cookieUpdateTest", "a");
        response.addCookie(cookie);

        return "index";
    }

    @GetMapping("/place/{placeId}")
    public String logTest(Model model, @PathVariable Long placeId,
                           HttpServletRequest request, HttpServletResponse response){
        //PlaceDto placeDto = placeService.getPlaceTest3Info();
//        PlaceDto placeDto = placeService.getPlaceInfo(placeId);

        //조회를 할 때마다 response 업데이트 해주기
        Cookie cookie = new Cookie("recentlyViewProperty", "{" +
                "\"propertyId\": \"" + placeId + "\"," +
                "\"propertyType\": \"place\"" +
                "}");
        cookie.setMaxAge(5 * 60);
        response.addCookie(cookie);

        return "index";
    }


//    @GetMapping("/place/location/{placeId}")
//    public String placeLocation(Model model, @PathVariable Long placeId){
//        Location location = placeService.getLocation(placeId);
//
//        model.addAttribute("location", location);
//
//        return "locations";
//    }

//    @GetMapping("/place/{placeId}")
//    public String place(@PathVariable Long placeId,
//                        @RequestParam @Nullable String checkinDate,
//                        @RequestParam @Nullable String checkoutDate,
//                        Model model){
//        PlaceDto placeDto = placeService.getPlaceInfo(placeId, checkinDate, checkoutDate);
//
//        model.addAttribute("placeDto", placeDto);
//        return "places";
//    }
}

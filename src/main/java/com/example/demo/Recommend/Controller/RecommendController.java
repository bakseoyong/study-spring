package com.example.demo.Recommend.Controller;

import com.example.demo.Auth.Domain.GuestAuth;
import com.example.demo.Recommend.Dto.RecommendPropertyDto;
import com.example.demo.Recommend.Service.RecommendRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class RecommendController {
    private final Logger logger = LoggerFactory.getLogger("searchCookieTest");
    private RecommendRegistry recommendRegistry;

    /**
     * 원래 조회할때 같이 로깅에 남겨야 되지만 테스트 용으로 recommendController에 연관상품 관련 로깅을 하는 컨트롤러 만들기
     */
    @GetMapping("/recommend/logging")
    public String recommendLoggingTest(){


        return "index";
    }

    //test code 테스트 위한 api
    @GetMapping("/recommend2/")
    public String getCartItemsTest2(String propertyId, String propertyType){
        if(propertyId == null){
            return "index"; //추천 알고리즘 없는 일반 페이지
        }

        //연관된 프로퍼티 자동으로 가져오기
        List<RecommendPropertyDto> recommendPropertyDtos =
                recommendRegistry.getServiceBean(propertyType).showRelatedRecommendProperties(Long.parseLong(propertyId));

        return "index";
    }


    //postman으로 테스트하기 위한 목적의 api
    @GetMapping("/recommend/")
    public String getCartItemsTest(Model model,
                                   @CookieValue(value = "clientId", required = false) Cookie cookie,
                                   @CookieValue(value = "recentlyPropertyId", required = false) Cookie cookie2,
                                   HttpServletResponse response, HttpSession session){

        //변수명이 프로퍼티인 이유 : 숙박, 레저 둘 다 연관 상품을 제공하기 때문에.
        String clientId = GuestAuth.getIdentifier(cookie, response);

        //recentlyProperty에 id와 type만 들어있기 떄문에 objectMapper까지 써야될가 싶긴 한데... 나중에 추가될 수 있으니까 이렇게 만들어 놓자.
        String value = cookie2.getValue();

        ObjectMapper mapper = new ObjectMapper();

        try{
            Map<String, String> map = mapper.readValue(value, Map.class);

            String id = map.get("propertyId");
            String type = map.get("propertyType");
            //조회한 프로퍼티가 없다. => 흔히 발생할 수 있는 경우
            if(map.get("propertyId") == null){
                return "index"; //추천 알고리즘 없는 일반 페이지
            }

            //연관된 프로퍼티 자동으로 가져오기
            List<RecommendPropertyDto> recommendPropertyDtos =
                    recommendRegistry.getServiceBean(type).showRelatedRecommendProperties(Long.parseLong(id));

        }catch (IOException e){
            e.printStackTrace();
        }


        return "index";
    }
}

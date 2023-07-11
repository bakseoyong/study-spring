package com.example.demo.Reservation.Controller;

import com.example.demo.Place.Domain.Place;
import com.example.demo.Reservation.Dto.*;
import com.example.demo.Reservation.Service.ReservationServiceImpl;
import com.example.demo.utils.Exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JSONArray;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/reservation/*")
public class ReservationController {
    private ReservationServiceImpl reservationService;

    @GetMapping("/test")
    public String newReservationTest(){
        return "newReservation";
    }

//    @PostMapping("/api/v1/leisure/new")
//    public ResponseEntity<Object> loadLeisureReservationPage(
//            @RequestBody String jsonArray){
//
//        Gson gson = new Gson();
//        List<Map<String, Object>> leisureTickets =
//                gson.fromJson(jsonArray, new TypeToken<List<Map<String, Object>>>(){}.getType());
//
//        if(leisureTickets.size() == 0){
//            throw new BusinessException();
//        }
//
//        final HttpHeaders headers = new HttpHeaders();
//
//        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance().path("/reservation/new");
//        uriComponentsBuilder.queryParam("type", "leisure");
//
//        for (Map<String, Object> leisureTicket : leisureTickets){
//            uriComponentsBuilder = uriComponentsBuilder
//                    .queryParam("id", leisureTicket.get("id"))
//                    .queryParam("quantity", leisureTicket.get("quantity"));
//        }
//
//        headers.setLocation(uriComponentsBuilder.build().toUri());
//
//        return ResponseEntity.status(HttpStatus.SEE_OTHER)
//                .headers(headers)
//                .build();
//    }

    @GetMapping("/new/leisure")
    public ResponseEntity<Object> renderLeisure(String json){
        Gson gson = new Gson();
        Map<String, Object> attributes =
                gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());

        //여기서 파싱을 하는 이유 : 요청된 데이터의 검증을 위해서
        if(attributes.size() == 0){
            throw new IllegalArgumentException();
        }

        ReservationPageDto reservationPageDto = reservationService.reservationPage2(attributes);


        return ResponseEntity.ok()
                .body(reservationPageDto);
    }

//    @PostMapping("/api/v1/place/new")
//    public ResponseEntity<Object> loadPlaceReservationPage(
//            @RequestBody PlaceReservationPageRequestDto placeReservationPageRequestDto){
//        // ratePlanId는 서비스에서 가져와야 겠다.
//        final HttpHeaders headers = new HttpHeaders();
//
//        headers.setLocation(UriComponentsBuilder.newInstance().path("/reservation/new")
//                .queryParamIfPresent("lng", Optional.ofNullable(placeReservationPageRequestDto.getLng()))
//                .queryParamIfPresent("lat", Optional.ofNullable(placeReservationPageRequestDto.getLat()))
//                .queryParam("type", "place")
//                .queryParam("id", placeReservationPageRequestDto.getId())
//                .queryParam("roomDetailId", placeReservationPageRequestDto.getRatePlanId())
//                .queryParam("ratePlanId", )
//                .queryParam("checkinDateTIme", placeReservationPageRequestDto.getCheckinDateTime())
//                .queryParam("checkoutDateTime", placeReservationPageRequestDto.getCheckoutDateTime())
//                .queryParam("adult", placeReservationPageRequestDto.getAdult())
//                .queryParamIfPresent("child", Optional.ofNullable(placeReservationPageRequestDto.getChild()))
//                .build()
//        );
//
//        return ResponseEntity.status(HttpStatus.SEE_OTHER)
//                .headers(headers)
//                .build();
//    }

    /**
     * render로 된거 하나로 합치기
     */
//    @GetMapping("/reservation/new?type=place")
//    public ResponseEntity<Object> renderReservationPage(){
//        Object mock = null;
//
//        return ResponseEntity.ok(mock);
//    }
//
//    @GetMapping("/reservation/new?type=leisure")
//    public ResponseEntity<Object> renderReservationPage(){
//        Object mock = null;
//
//        return ResponseEntity.ok(mock);
//    }

//    @GetMapping("/reservation/new")
//    public ResponseEntity<Object> renderReservationPage(){
//
//    }


    @GetMapping("/new")
    public String getNew(
            @RequestParam("type") String type,
            ReservationPageRequestDto reservationPageRequestDto,
            Model model
            ){
        ReservationPageDto reservationPageDto =
                reservationService.reservationPage(reservationPageRequestDto);
//        NewReservationDto newReservationDto = reservationServiceImpl.reservationPage(newReservationRequestDto);
        model.addAttribute("newReservationDto", reservationPageDto);
        return "newReservation";
    }

    @GetMapping("/new/discounts")
    public String getDiscounts(String type){


//        List<DiscountDto> discountDtos =
//            reservationRegistry.getServiceBean(type).getDiscounts(discountRequestDto);
        return "index";
    }

    @PostMapping(value = "/api/v1/payment/success")
    public String roomSuccessfullyReserved(@RequestParam("type") String type,
                                           ReservationCreateRequestDto reservationSuccessDto){
        reservationService.createReservation(reservationSuccessDto);
        return "reservationSuccess";
    }

    @GetMapping("/history/domestic")
    public void domesticReservations(){
        //최근 2년간 예약 내역에 대해 출력해준다.
        //place type 보다 더 높은 개념. Place

        //단순 디비 조회. 도메인 영역에서 할게 없다.
        //reservationService.getUserReservationHistroy(userId);
    }

//    @PostMapping("/reservation/new")
//    public String
}

//
//https://platform-site.yanolja.com/orders/
//new?0[
//        lng]=127.06624&0[
//        lat]=37.50681&1[
//        type%5D=property&1[
//        propertyId%5D=3006234&1[ - 장소 id
//        useType%5D=D&1[
//        roomTypeId%5D=95118&1%5B - 룸 id (사장님 기능과 관련된 듯)
//        ratePlanId%5D=24223&1%5B - 요금제 id
//        ratePlanVersion%5D=2&1%5B - 요금제 version
//        checkInDate%5D=2022-10-18&1%5B
//        checkOutDate%5D=2022-10-20&1%5B
//        checkInAt%5D=2022-10-18T15%3A00%3A00.000%2B09%3A00&1%5B - 이전 페이지 데이터를 가져와서 쓰나보다
//        checkOutAt%5D=2022-10-20T11%3A00%3A00.000%2B09%3A00&1%5B
//        adults%5D=2
//-> percent encoding, url encoding



package com.example.demo.Reservation.Controller;

import com.example.demo.Reservation.Dto.NewReservationDto;
import com.example.demo.Reservation.Dto.NewReservationRequestDto;
import com.example.demo.Reservation.Dto.ReservationCreateRequestDto;
import com.example.demo.Reservation.Service.ReservationRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservation/*")
public class ReservationController {
    private ReservationRegistry reservationRegistry;

    @GetMapping("/test")
    public String newReservationTest(){
        return "newReservation";
    }

    @GetMapping("/new")
    public String getNew(
            @RequestParam("type") String type,
            NewReservationRequestDto newReservationRequestDto,
            Model model
            ){
        NewReservationDto newReservationDto =
                reservationRegistry.getServiceBean(type).reservationPage(newReservationRequestDto);
//        NewReservationDto newReservationDto = reservationServiceImpl.reservationPage(newReservationRequestDto);
        model.addAttribute("newReservationDto", newReservationDto);
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
        reservationRegistry.getServiceBean(type).createReservation(reservationSuccessDto);
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



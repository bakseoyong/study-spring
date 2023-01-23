package com.example.demo.Cart.Controller;

import com.example.demo.Auth.Domain.GuestAuth;
import com.example.demo.Cart.Domain.*;
import com.example.demo.Cart.Dto.BusCartDto;
import com.example.demo.Cart.Dto.TrainCartDto;
import com.example.demo.Cart.Service.CartService;
import com.example.demo.Leisure.Domain.LeisureCartDto;
import com.example.demo.Leisure.Domain.LeisureTicket;
import com.example.demo.Leisure.Repository.LeisureTicketRepository;
import com.example.demo.Review.Dto.TestDto;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Repository.RoomRepository;
import com.example.demo.utils.Converter.CartSessionConverter;
import com.example.demo.utils.Session.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private LeisureTicketRepository leisureTicketRepository;
    @Autowired
    private RoomRepository roomRepository;


    @GetMapping("/cart")
    public String getCartItems(Model model, @CookieValue(value = "clientId", required = false) Cookie cookie,
                               HttpServletResponse response, HttpSession session){
//        String id = GuestAuth.getIdentifier(cookie, response);
//        //cartItem으로 묶어서 저장/조회 할지 . 나눠서 저장/조회 할지.
//
//        String dbData = getAlreadyCartItems(id, session);
//        List<Cartable> redisItems = CartSessionConverter.convertToEntityAttribute(dbData);
//        List<CartItem> cartItems = new ArrayList<>();
//
//        //  items 에 ticketName, leisureName을 집어 넣어야 한다.
//        for(Cartable item: redisItems){
//            String type = item.getType();
//
//            switch (type){
//                case "place":
//                    PlaceCartRedisDto placeCartRedisDto = (PlaceCartRedisDto) item;
//
//                    Room room = roomRepository.findById(placeCartRedisDto.getRoomId())
//                            .orElseThrow(EntityNotFoundException::new);
//
//                    PlaceCartDto placeCartDto = PlaceCartDto.builder()
//                            .placeCartRedisDto(placeCartRedisDto)
//                            .checkinAt(room.getCheckinAt())
//                            .checkoutAt(room.getCheckoutAt())
//                            .placeName(room.getPlace().getName())
//                            .roomName(room.getName())
//                            .address(room.getPlace().getAddress())
//                            .standardPersonNum(room.getStandardPersonNum())
//                            .maximumPersonNum(room.getMaximumPersonNum())
//                            .build();
//
//                    // price 관련 코드 가져오기
//
//                    cartItems.add(placeCartDto);
//                    break;
//                case "leisure":
//                    //LeisureCartDto, LeisureExtraInfo => LeisureCartRedisDto , LeisureCartDto
//                    //redisDto -> implements 'Cartable' , cartDto -> implements 'CartItem'
//                    LeisureCartRedisDto leisureCartRedisDto = (LeisureCartRedisDto) item;
//                    LeisureTicket leisureTicket = leisureTicketRepository.findById(leisureCartRedisDto.getTicketId())
//                            .orElseThrow(EntityNotFoundException::new);
//                    LeisureCartDto leisureCartDto = new LeisureCartDto();
//                    leisureCartDto.setLeisureCartRedisDto(leisureCartRedisDto);
//                    leisureCartDto.setLeisureName(leisureTicket.getLeisure().getName());
//                    leisureCartDto.setLeisureTicketName(leisureTicket.getName());
//
//                    cartItems.add(leisureCartDto);
//                    break;
//                case "transportation":
//                    TransportationCartRedisDto transportationCartRedisDto = (TransportationCartRedisDto) item;
//                    String specific = transportationCartRedisDto.getSpecific();
//                    switch (specific){
//                        case "bus":
//                            BusCartDto busCartDto = (BusCartDto) item;
//                            busCartDto.setBrandImgPath("test_path");
//                            //Bus bus = busRepository.findById(transportationCartDto.getDptCode());
//                            cartItems.add(busCartDto);
//                            break;
//                        case "train":
//                            TrainCartDto trainCartDto = (TrainCartDto) item;
//                            trainCartDto.setBrandImgPath("test_path");
//                            cartItems.add(trainCartDto);
//                            break;
//                    }
//                    break;
//            }
//        }
//
//
//
//
//        model.addAttribute("cartItems", items);
//
        return "myCart";
    }

    @GetMapping("/carttest")
    public String getCartItemsTest(Model model, @CookieValue(value = "clientId", required = false) Cookie cookie,
                                   HttpServletResponse response, HttpSession session){
        String id = GuestAuth.getIdentifier(cookie, response);

        TestDto testDto = (TestDto) session.getAttribute("testDto");
        System.out.println(testDto.getRoomName());

        return "index";
    }

    //세션에 객체가 잘 담기는지 테스트해보기
    @GetMapping("/carttest2")
    public String objectSessionTest(HttpSession session, HttpServletResponse response){
        TestDto testDto = new TestDto();
        testDto.setRoomName("testRoomName");
        session.setAttribute("testDto", testDto);

        TestDto t2 = (TestDto) session.getAttribute("testDto");
        System.out.println("t2's room name is : " + t2.getRoomName());

        return "index";
    }

    @PostMapping(value = "/api/v1/cart/save", params = {"place"})
    public String addPlaceToCart(HttpSession session, @RequestParam String place,
                                 @RequestBody PlaceCartRedisDto placeCartRedisDto,
                                 @CookieValue("clientId") Cookie cookie, HttpServletResponse response
                                 ){
        String id = GuestAuth.getIdentifier(cookie, response);

        String alreadyCartItems = SessionUtils.getAlreadyCartItems(id, session);
        session.setAttribute(id + ".cart", alreadyCartItems.concat(placeCartRedisDto.toSession()));

        return "index";
    }

    @PostMapping(value = "/api/v1/cart/save", params = {"leisure"})
    public String addLeisureToCart(HttpSession session, @RequestParam String leisure,
                                   @RequestBody LeisureCartRedisDto leisureCartRedisDto,
                                   @CookieValue("clientId") Cookie cookie, HttpServletResponse response){
        String id = GuestAuth.getIdentifier(cookie, response);
        String alreadyCartItems = SessionUtils.getAlreadyCartItems(id, session);
        session.setAttribute(id + ".cart", alreadyCartItems.concat(leisureCartRedisDto.toSession()));

        return "index";
    }

    @PostMapping(value = "/api/v1/cart/save", params = {"transportation"})
    public String addTransportationToCart(HttpSession session, @RequestParam String transportation,
                                 @RequestBody TransportationCartRedisDto transportationCartRedisDto,
                                 @CookieValue("clientId") Cookie cookie,
                                 HttpServletResponse response){
        String id = GuestAuth.getIdentifier(cookie, response);

        String alreadyCartItems = SessionUtils.getAlreadyCartItems(id, session);
        session.setAttribute(id + ".cart", alreadyCartItems.concat(transportationCartRedisDto.toSession()));

        return "index";
    }

    /**
     * 혹시 모르니까 주석처리
     */
//    @PostMapping(value = "/api/v1/cart/save", params = {"train"})
//    public String addTrainToCart(HttpSession session, @RequestParam String train,
//                                 @RequestBody TransportationCartDto transportationCartDto,
//                                 @CookieValue("clientId") Cookie cookie,
//                                 HttpServletResponse response){
//        String id = GuestAuth.getIdentifier(cookie, response);
//
//        String alreadyCartItems = getAlreadyCartItems(id, session);
//        session.setAttribute(id + ".cart", alreadyCartItems.concat(transportationCartDto.toSession()));
//
//        return "index";
//    }
//
//    @PostMapping(value = "/api/v1/cart/save", params = {"bus"})
//    public String addBusToCart(HttpSession session, @RequestParam String bus,
//                               @RequestBody TransportationCartDto transportationCartDto,
//                               @CookieValue("clientId") Cookie cookie,
//                               HttpServletResponse response){
//
//        String id = GuestAuth.getIdentifier(cookie, response);
//
//        String alreadyCartItems = getAlreadyCartItems(id, session);
//        session.setAttribute(id + "cart", alreadyCartItems.concat(transportationCartDto.toSession()));
//
//        return "index";
//    }
}

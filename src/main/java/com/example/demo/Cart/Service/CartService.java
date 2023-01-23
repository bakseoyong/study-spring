package com.example.demo.Cart.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CartService {
    //세가지 타입에서 어떤 정보들이 들어가는지 먼저 확인해 보자
    //교통 : 항공권 - 예약아님, 철도 - 야놀자에서 가능, 렌터카 - 예약이 티켓으로 잡힌다.
    //버스 - 편도or왕복 , 브랜드이미지파일,  출발지, 도착지, 출발시간, 도착시간, 날짜, 우등or일반or프리미엄, 좌석(11번좌석), 성인요금, 인원수
    //철도 - 편도or왕복, 브랜드이미지파일, 출발지, 도착지, 출발시간, 도착시간, 날짜, 기차번호, 임의배정or좌석(7호차14A), 성인요금, 인원수
    //=> 예약 형식은 이렇게

//    roomId, leisureId 까지는 괜찮은데... 교통은 아이디로 작성하기엔 너무 많다.
//    교통은 예외 케이스로 두고, roomId와 leisureId는 기존 도메인에서 받아서 처리.
//      그러면 cartItem이라는 객체를 만들어야 할까 => 만들어야 한다. 데이터 분석을 위해서
//      카트 item에는 rooms, leisures, transportations가 있고 최대 20개의 상품을 담을 수 있게 해야 한다.


//    public List<CartItem> getCartItems(String identifier){
//        List<CartItem> cartItems = cartRepository.getCartItemByIdentifier(identifier);
//
//        return cartItems;
//    }


//    public void addPlaceToCart(){
//
//    }
//
//    public void addLeisureToCart(){
//
//    }
//
////    transportation 인터페이스를 만들어서 대비해야 겠다.... 지금 사용하는 행동에만 초점을 둬서
//    public List<Transportation> addTransportationToCart(List<Transportation> transportations, Transportation newTransportation){
//        transportations.add(newTransportation);
//        return transportations;
//    }
}

package com.example.demo;

import com.example.demo.Place.Domain.FacilitiesService;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Domain.PlaceType;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Reservation.Domain.ReservationStatus;
import com.example.demo.Reservation.Repository.ReservationRepository;
import com.example.demo.Review.Policy.Inner14DaysAfterCheckout;
import com.example.demo.Review.Policy.NonConsumerNotAllowed;
import com.example.demo.Review.Policy.WritableCategory;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomType;
import com.example.demo.Room.Repository.RoomRepository;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Domain.User;
import com.example.demo.User.Repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@Transactional
public class ReviewPolicyTest {
//    정책들을 리스트로 묶어 빈객체로 등록한 뒤 호출하면 편할 것 같다....
//    => 어떻게 하지

    private Inner14DaysAfterCheckout inner14DaysAfterCheckout;
    private WritableCategory writableCategory;
    private NonConsumerNotAllowed nonConsumerNotAllowed;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void SetUp(){
        Consumer consumer = Consumer.builder()
                .loginId("consumerTest")
                .password("qwer1234")
                .email("test@test.com")
                .build();

        em.persist(consumer);
        System.out.println("consumer id is : " + consumer.getId());

        Place place = Place.builder()
                .placeType(PlaceType.호텔)
                .name("테스트호텔")
                .address("테스트광역시 테스트구 테스트동 1-1")
                .facilitiesServices(FacilitiesService.노래방 + "." + FacilitiesService.사우나)
                .build();

        em.persist(place);

        Room room = Room.builder()
                .roomType(RoomType.숙박)
                .place(place)
                .name("테스트룸")
                .weekdayPrice(80000L)
                .fridayPrice(80000L)
                .weekendPrice(80000L)
                .standardPersonNum(2L)
                .maximumPersonNum(2L)
                .noSmoking(true)
                .information("와인 무료제공 스탠다드 트윈")
                .checkinAt(LocalTime.of(15, 0))
                .build();

        room.setPlace(place);
        place.addRoom(room);
        em.persist(room);

        Reservation reservation = Reservation.builder()
                .room(room)
                .checkinDate(LocalDate.of(2022, 11, 3))
                .checkoutDate(LocalDate.of(2022, 11, 5))
                .personNum(2L)
                .contractorName("박테스트")
                .phone("010-0101-0202")
                .build();

        reservation.setReservationStatus(ReservationStatus.체크아웃);
        //단방향 연관관계 <= null not allowed for column "USER_ID" 에러 해결하기
        reservation.setGuest(consumer);

        em.persist(reservation);
        System.out.println("Reservation id is : " + reservation.getId());

        room.addReservation(reservation);
        reservation.setRoom(room);

        em.persist(reservation);

        reservationRepository.save(reservation);

        Assert.assertNotNull(consumer.getId());

        /**
         * Policies
         */
        inner14DaysAfterCheckout = new Inner14DaysAfterCheckout();
        writableCategory = new WritableCategory();
        nonConsumerNotAllowed = new NonConsumerNotAllowed();
    }

    @AfterEach
    public void afterEach(){
        List<User> users = userRepository.findAll();
        System.out.println("before delete, user size is : " + users.size());
        System.out.println("consumer login id is : " + users.get(0).getLoginId());
        //deleteAll이 안된다면, deleteById는 될까?
        //userRepository.deleteById(users.get(0).getId()); //정상적으로 실행된다.

        //delete from users where user_id=?
        reservationRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void SetUp_테스트(){
        /**
         * roomRepository deleteAll 을 실행시킬때 user까지 삭제하려고 하는 이유 찾기
         * cascade ALl 부분에서 생긴 것 같은데..
         *
         * Room entity의 cascade All 부분들을 Persist로 변경
         * => reservationRepository에서 delete할때 room id가 필요한데 room을 먼저 삭제해서 reservation에서 오류가 생김.
         * ==> reservation은 room과 all관계가 없는데...
         *
         * roomRepository에서 에러가 발생하는데 오류가 나는 코드를 없애서 오류가 안뜨는거 아닐까...
         * => size 출력해보자
         *
         * ===> size가 둘 다 1로 뜨는걸 보아 해결된게 아니라 오류가 나는 코드를 없애서 오류가 안 발생가는것처럼 보이는듯
         *
         * constraint ["FKB5G9IO5H54IWL2INKNO50PPLN
         * : PUBLIC.RESERVATIONS FOREIGN KEY(USER_ID) REFERENCES PUBLIC.USERS(USER_ID) (CAST(1 AS BIGINT))";
         * => 외래키로 사용되고 있으니까 먼저 지울 수 없다!!!!!!
         *
         *
         * ==> 삭제 순서를 변경 후 테스트 성공!!!!!
         */

        //setup의 reservation인스턴스를 할당할때 빌더타입에 guest를 집어넣지 않아 user_id null 에러가 뜨길래 추가해줬다.
    }

    @Test
    public void 유저_테스트(){
        Consumer consumer = (Consumer) userRepository.findAll().get(0);
        System.out.println("consumer id is : " + consumer.getId());
    }


    @Test
    public void Inner14DaysAfterCheckout_테스트(){
//        Consumer consumer = (Consumer) userRepository.findAll().get(0);
//        System.out.println("consumer id is : " + consumer.getId());
        System.out.println("reservation size is  : " + reservationRepository.findAll().size());
        Reservation reservation = reservationRepository.findAll().get(0);

        System.out.println("policy result is : " + inner14DaysAfterCheckout.isSatisfied(reservation)); //true!!!
        Assert.assertEquals(true, inner14DaysAfterCheckout.isSatisfied(reservation));
    }

    @Test
    public void WritableCategory_테스트(){
        Reservation reservation = reservationRepository.findAll().get(0);

        Assert.assertEquals(true, writableCategory.isSatisfied(reservation));
    }

    @Test
    public void NonConsumerNotAllowed_테스트(){
        Reservation reservation = reservationRepository.findAll().get(0);

        Assert.assertEquals(true, nonConsumerNotAllowed.isSatisfied(reservation));
    }
}

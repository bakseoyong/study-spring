package com.example.demo;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Billing.Domain.BillingStatus;
import com.example.demo.Business.Domain.Business;
import com.example.demo.Business.Domain.BusinessType;
import com.example.demo.Repositories.BusinessRepository;
import com.example.demo.Repositories.ReservationRepository;
import com.example.demo.Repositories.RoomRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Reservation.Domain.ImPortResponse;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Reservation.Dto.ReservationCancelDto;
import com.example.demo.Reservation.Dto.ReservationCancelRequestDto;
import com.example.demo.Reservation.Dto.ReservationCreateRequestDto;
import com.example.demo.Reservation.Dto.ReservationSuccessDto;
import com.example.demo.Reservation.Service.ReservationService;
import com.example.demo.Room.Domain.FacilitiesService;
import com.example.demo.Room.Domain.FakeRoom;
import com.example.demo.Room.Domain.RemainingRoom;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Repository.RemainingRoomMongoRepository;
import com.example.demo.Room.Service.RemainingRoomService;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Domain.NonConsumer;
import com.example.demo.User.Domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ReservationTest {
    private static Business business;
    private static Room room;
    private static Consumer consumer;
    private static User nonConsumer;

    private static List<RemainingRoom> remainingRooms;

    @Mock
    private RemainingRoomMongoRepository remainingRoomMongoRepository;

    @Mock
    private RoomRepository roomRepository;

    //@Autowired
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    //@Autowired
    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp(){
        business = Business.builder()
                .businessType(BusinessType.??????)
                .name("???????????????")
                .address("?????????????????? ???????????? ???????????? 1-1")
                .facilitiesServices(FacilitiesService.????????? + "." + FacilitiesService.?????????)
                .build();

        room = Room.builder()
                .business(business)
                .name("????????????")
                .standardPrice(80000L)
                .standardPersonNum(2L)
                .maximumPersonNum(2L)
                .noSmoking(true)
                .information("?????? ???????????? ???????????? ??????")
                .checkinStarted(LocalTime.of(15, 0))
                .build();

        consumer = Consumer.builder()
                .id("consumerTest")
                .password("qwer1234")
                .email("test@test.com")
                .build();

        nonConsumer = NonConsumer.toEntity();

        remainingRooms = new ArrayList<>();
        LocalDate started = LocalDate.of(2022, 10, 1);
        LocalDate ended = LocalDate.of(2022, 10, 6);
        for(LocalDate date = started; date.isBefore(ended); date = date.plusDays(1)){
            RemainingRoom remainingRoom = RemainingRoom.builder()
                    .roomId(room.getId())
                    .date(date)
                    .numberOfRemainingRoom(5)
                    .build();
            remainingRooms.add(remainingRoom);
        }
    }

    @Test
    public void ?????????_??????_??????_????????????(){
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            Reservation reservation = Reservation.builder()
                    .contractorName("?????????")
                    .phone("010-0101-0101")
                    .checkinAt(LocalDate.of(2022, 10, 25))
                    .checkoutAt(LocalDate.of(2022, 10, 29))
                    .personNum(3L)
                    .room(room)
                    .build();
        });

        assertEquals(exception.getMessage(), "???????????? ?????? ?????????????????? ????????? ???????????????.");
    }

    @Test
    public void ?????????_??????_??????_API(){

        //???????????? ?????? ?????? ??????.
        ImPortResponse imPortResponse = ImPortResponse.builder()
                .success(true)
                .merchant_uid("A1")
                .pay_method("card")
                .paid_amount(200000L)
                .build();

        ReservationCreateRequestDto reservationCreateRequestDto = ReservationCreateRequestDto.builder()
                .isConsumer(false)
                .contractorName("?????????")
                .phone("010-0101-0101")
                .checkinAt(LocalDate.of(2022, 10, 1))
                .checkoutAt(LocalDate.of(2022, 10, 3))
                .personNum(1L)
                .roomId(1L) //room.getId??? ????????? mock ????????? ???????????? id??? ???????????? ?????????.
                .imPortResponse(imPortResponse)
                .build();

        ReservationSuccessDto reservationSuccessDto = ReservationSuccessDto.builder()
                .businessName("???????????????")
                .roomName("????????????")
                .checkinAt(LocalDate.of(2022,10,1))
                .checkoutAt(LocalDate.of(2022, 10, 3))
                .price(200000L)
                .build();
        doReturn(Optional.of(room)).when(roomRepository).findById(any(Long.class));
        //doReturn(Optional.of(nonConsumer)).when(userRepository).findById(any(Long.class));

        //?????? ???????????? argumentMatcher??? ???????????? ????????? 'Invalid use of argument matchers!' ?????? ??????
        doReturn(remainingRooms).when(remainingRoomMongoRepository).isRoomSoldOutBetweenStartedAtAndEndedAt(
                any(Long.class), any(LocalDate.class), any(LocalDate.class)
        );
        doReturn(remainingRooms).when(remainingRoomMongoRepository).saveAll(any(List.class));
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());
        when(roomRepository.save(any(Room.class))).then(AdditionalAnswers.returnsFirstArg());

        ReservationSuccessDto testResult = reservationService.createReservation(reservationCreateRequestDto);

        assertEquals(reservationSuccessDto.getBusinessName(), testResult.getBusinessName());
        assertEquals(reservationSuccessDto.getPrice(), testResult.getPrice());
        assertEquals(reservationSuccessDto.getCheckinAt(), testResult.getCheckinAt());
        assertEquals(reservationSuccessDto.getCheckoutAt(), testResult.getCheckoutAt());
        assertEquals(reservationSuccessDto.getRoomName(), testResult.getRoomName());
    }

    @Test
    public void ?????????_??????_??????_API(){
        ImPortResponse imPortResponse = ImPortResponse.builder()
                .success(true)
                .paid_amount(200000L)
                .merchant_uid("A1")
                .pay_method("card")
                .build();

        ReservationCancelRequestDto reservationCancelRequestDto = ReservationCancelRequestDto.builder()
                .reservationId(1L)
                .billingId(1L)
                .imPortResponse(imPortResponse)
                .build();

        FakeRoom fakeRoom = new FakeRoom(1L, 2L, business, "????????????");

        Reservation reservation = Reservation.builder().guest(nonConsumer)
                .personNum(2L)
                .room((Room) fakeRoom)
                .checkinAt(LocalDate.of(2022, 10, 1))
                .checkoutAt(LocalDate.of(2022, 10, 3))
                .build();

        Billing billing = Billing.builder()
                .reservation(reservation)
                .billingStatus(BillingStatus.????????????)
                .price(imPortResponse.getPaid_amount())
                .build();

        reservation.addBilling(billing);

        ReservationCancelDto reservationCancelDto = ReservationCancelDto.builder()
                .businessName("???????????????")
                .roomName("????????????")
                .checkinAt(LocalDate.of(2022,10,1))
                .checkoutAt(LocalDate.of(2022, 10, 3))
                .price(200000L)
                .build();

        doReturn(Optional.of(reservation)).when(reservationRepository).findById(any(Long.class));
        doReturn(remainingRooms).when(remainingRoomMongoRepository).isRoomSoldOutBetweenStartedAtAndEndedAt(
                any(Long.class), any(LocalDate.class), any(LocalDate.class)
        );
        doReturn(remainingRooms).when(remainingRoomMongoRepository).saveAll(any(List.class));
        when(reservationRepository.save(any(Reservation.class))).then(AdditionalAnswers.returnsFirstArg());

        ReservationCancelDto testResult = reservationService.cancelReservation(reservationCancelRequestDto);

        assertEquals(reservationCancelDto.getBusinessName(), testResult.getBusinessName());
        assertEquals(reservationCancelDto.getPrice(), testResult.getPrice());
        assertEquals(reservationCancelDto.getCheckinAt(), testResult.getCheckinAt());
        assertEquals(reservationCancelDto.getCheckoutAt(), testResult.getCheckoutAt());
        assertEquals(reservationCancelDto.getRoomName(), testResult.getRoomName());
    }

    @Test
    public void ??????_??????_??????(){
        Room room = roomRepository.findAll().get(0);

        Consumer consumer = (Consumer) userRepository.findAll().get(0);

        Reservation reservation = Reservation.builder()
                .guest(consumer)
                .phone("010-0101-0101")
                .checkinAt(LocalDate.of(2022, 8, 25))
                .checkoutAt(LocalDate.of(2022, 8, 29))
                .personNum(1L)
                .room(room)
                .build();
    }


}

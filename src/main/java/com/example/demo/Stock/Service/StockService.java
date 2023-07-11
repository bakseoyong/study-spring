package com.example.demo.Stock.Service;

import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponMiddleTable;
import com.example.demo.Coupon.Domain.CouponOwner;
import com.example.demo.Reservation.Domain.LeisureReservation;
import com.example.demo.Reservation.Domain.LeisureReservationDetail;
import com.example.demo.Reservation.Domain.PlaceReservation;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.Room.Repository.RoomDetailRepository;
import com.example.demo.Stock.Domain.*;
import com.example.demo.Stock.Event.OutOfCouponStockEvent;
import com.example.demo.Stock.Repository.RoomDetailStockRepository;
import com.example.demo.Stock.Repository.StockRepository;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RoomDetailRepository roomDetailRepository;
    private final StockRepository stockRepository;
    private final RoomDetailStockRepository roomDetailStockRepository;

    public void create(Long roomDetailId, LocalDate start, LocalDate end, Long total){
        RoomDetail roomDetail = roomDetailRepository.findById(roomDetailId)
                .orElseThrow(EntityNotFoundException::new);

        List<RoomDetailStock> already = roomDetailStockRepository.findAlreadyStocks(roomDetailId, start, end);

        Map<LocalDate, RoomDetailStock> search = already.stream()
                .collect(Collectors.toMap(RoomDetailStock::getDate, Function.identity()));

        List<RoomDetailStock> roomDetailStocks = new ArrayList<>();

        for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            RoomDetailStock roomDetailStock = search.get(date);
            if(roomDetailStock == null) {
                roomDetailStocks.add(RoomDetailStock.of(roomDetail, date, total));
            }else{
                roomDetailStock.update(total);
            }
        }

        roomDetailStockRepository.saveAll(roomDetailStocks);
    }

    public void reserved(Reservation reservation){
        switch (reservation.getClass().getName()){
            case "PlaceReservation":
                PlaceReservation placeReservation = (PlaceReservation) reservation;
                RoomDetail roomDetail = placeReservation.getRoomDetail();
                for(LocalDate d = placeReservation.getCheckinDateTime().toLocalDate();
                    d.isBefore(placeReservation.getCheckoutDateTime().toLocalDate());
                    d.plusDays(1)){

                    RoomDetailStock roomDetailStock = roomDetail.findRoomDetailStockByDate(d);
                    roomDetailStock.reserved();
                }
                break;

            case "LeisureReservation":
                List<LeisureReservationDetail> leisureReservationDetails =
                        ((LeisureReservation)reservation).getLeisureReservationDetails();

                for(LeisureReservationDetail l: leisureReservationDetails){
                    LeisureTicketStock leisureTicketStock = l.getLeisureTicket().getLeisureTicketStock();
                    for(int i = 0; i < l.getQuantity(); i++)
                        leisureTicketStock.reserved();
                }
                break;

            default:
                throw new BusinessException(ErrorCode.NOT_SERVICED_RESERVATION_TYPE);
        }
    }

    public void canceled(Reservation reservation){
        //재고 취소
        switch (reservation.getClass().getName()){
            case "PlaceReservation":
                RoomDetail roomDetail = ((PlaceReservation)reservation).getRoomDetail();
                RoomDetailStock roomDetailStock = roomDetail.findStockByDate();
                roomDetailStock.cancelReservation(checkinDateTime, checkouDateTime);
                break;

            case "LeisureReservation":
                List<LeisureReservationDetail> leisureReservationDetails =
                        ((LeisureReservation)reservation).getLeisureReservationDetails();

                for(LeisureReservationDetail l: leisureReservationDetails){
                    LeisureTicketStock leisureTicketStock = l.getLeisureTicket().getLeisureTicketStock();
                    for(int i = 0; i < l.getQuantity(); i++)
                        leisureTicketStock.cancel();
                }
                break;

            default:
                throw new BusinessException(ErrorCode.NOT_SERVICED_RESERVATION_TYPE);
        }
    }

    public void used(CouponOwner owner, Coupon coupon, Reservation reservation){
        CouponMiddleTable cmt = owner.findCouponMiddleTableByCoupon(coupon);

        CouponStock cacheStock = cmt.getCouponStock();

        CouponStock stock = (CouponStock) stockRepository.findById(cacheStock.getId());

        try {
            stock.reserved();
        }catch (BusinessException e){
            OutOfCouponStockEvent event = new OutOfCouponStockEvent(reservation);
            applicationEventPublisher.publishEvent(event);
        }
    }

    private boolean validateCoupon(){
        CouponMiddleTable cmt = owner.findCouponMiddleTableByCoupon(coupon);

        CouponStock cacheStock = cmt.getCouponStock();

        CouponStock stock = (CouponStock) stockRepository.findById(cacheStock.getId());

        try {
            stock.reserved();
        }catch (BusinessException e){
            CouponValidationFailedEvent event = new CouponValidationFailedEvent(reservation);
            applicationEventPublisher.publishEvent(event);
        }
    }



}

package com.example.demo.Stock.Domain;

import com.example.demo.Room.Domain.RoomDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("RoomDetail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomDetailStock extends Stock{
    @NotNull
    @ManyToOne
    @JoinColumn(name = "room_detail_id")
    private RoomDetail roomDetail;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;


    public RoomDetailStock(@NotNull RoomDetail roomDetail, @NotNull LocalDate date,
                           @NotNull Long total, @NotNull Long remain) {
        super(total, remain);
        this.roomDetail = roomDetail;
        this.date = date;
    }

    public static RoomDetailStock of(RoomDetail roomDetail, LocalDate date, Long total){
        return new RoomDetailStock(roomDetail, date, total, total);
    }

}

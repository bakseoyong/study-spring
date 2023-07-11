package com.example.demo.Room.Domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@DiscriminatorValue("sugbak")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SugbakRoomDetail extends RoomDetail{
    protected SugbakRoomDetail(final SugbakRoomDetail.builder builder) {
        super(builder);
    }

    public static SugbakRoomDetail.builder builder() {
        return new SugbakRoomDetail.builder();
    }

    public static class builder extends RoomDetail.builder<SugbakRoomDetail.builder> {
        public SugbakRoomDetail build(){
            return new SugbakRoomDetail(this);
        }
    }
}

package com.example.demo.Room.Domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("dayuse")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DayUseRoomDetail extends RoomDetail{

    protected DayUseRoomDetail(final builder builder) {
        super(builder);
    }

    public static builder builder() {
        return new builder();
    }

    public static class builder extends RoomDetail.builder<builder> {
        public DayUseRoomDetail build() {
            return new DayUseRoomDetail(this);
        }
    }
}

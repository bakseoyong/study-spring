package com.example.demo.Room.Domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("InfinityDayUse")
public class InfinityDayUseRoomDetail extends DayUseRoomDetail implements InfinityCouponRoomDetail {

}

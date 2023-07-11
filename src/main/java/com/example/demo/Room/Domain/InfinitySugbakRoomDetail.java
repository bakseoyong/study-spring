package com.example.demo.Room.Domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * SugbakRoom - RoomDetail.class
 * AppliedInfinityCoupon - interface
 */
@Entity
@DiscriminatorValue("InfinitySugbak")
public class InfinitySugbakRoomDetail extends SugbakRoomDetail implements InfinityCouponRoomDetail {
}

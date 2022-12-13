package com.example.demo.Cart.Dto;

import com.example.demo.Cart.Domain.CartItem;
import com.example.demo.Cart.Domain.TransportationCartRedisDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BusCartDto extends TransportationCartRedisDto implements Serializable, CartItem {
    private String brandImgPath;

}

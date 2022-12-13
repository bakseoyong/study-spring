package com.example.demo.Cart.Dto;

import com.example.demo.Cart.Domain.CartItem;
import com.example.demo.Cart.Domain.TransportationCartRedisDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainCartDto extends TransportationCartRedisDto implements CartItem {
    private String brandImgPath;
}

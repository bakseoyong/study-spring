package com.example.demo.Leisure.Domain;

import com.example.demo.Cart.Domain.CartItem;
import com.example.demo.Cart.Domain.LeisureCartRedisDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeisureCartDto implements CartItem{
    private String leisureName;
    private String leisureTicketName;
    private LeisureCartRedisDto leisureCartRedisDto;

    @Override
    public String getType() {
        return leisureCartRedisDto.getType();
    }
}

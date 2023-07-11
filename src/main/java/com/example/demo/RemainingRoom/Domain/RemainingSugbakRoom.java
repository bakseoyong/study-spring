package com.example.demo.RemainingRoom.Domain;


import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "remaining_room")
@Getter
public class RemainingSugbakRoom extends RemainingRoom{
    public void reservation(){
        isSoldOut();
        super.decrease();
    }

    private void isSoldOut() throws RuntimeException{
        if(getNumberOfRemainingRoom() == 0)
            throw new RuntimeException("예약 정보 생성 중 방이 매진되었습니다.");
    }

}

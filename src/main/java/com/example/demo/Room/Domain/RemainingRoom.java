package com.example.demo.Room.Domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.ValidationException;
import java.time.LocalDate;

/**{
 *  {date : "2022.09.07", reservation : true},
 *  {date : "2022.09.08", reservation : false}
 *  }
 */

@Document(collection = "room_reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemainingRoom {
    @Id
    private String id;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int numberOfRemainingRoom;

    @Builder
    RemainingRoom(Long roomId, LocalDate date, int numberOfRemainingRoom){
        this.roomId = roomId;
        this.date = date;
        this.numberOfRemainingRoom = numberOfRemainingRoom;
    }

    public void setNumberOfRemainingRoom(int numberOfRemainingRoom) {
        if(this.numberOfRemainingRoom + numberOfRemainingRoom < 0L){
            throw new ValidationException("남은 방의 개수가 0 미만일 수 없습니다.");
        }
        this.numberOfRemainingRoom += numberOfRemainingRoom;
    }
}

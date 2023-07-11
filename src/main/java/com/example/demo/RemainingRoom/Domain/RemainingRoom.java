package com.example.demo.RemainingRoom.Domain;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDate;

@Document(collection = "remaining_room")
@Getter
public class RemainingRoom {
    public static final String ID = "id";
    public static final String ROOM_ID = "roomId";
    public static final String ROOM_DETAIL_ID = "roomDetailId";
    public static final String NUMBER_OF_REMAINING_ROOM = "numberOfRemainingRoom";
    public static final String DATE = "date";

    @Id
    private String id;

    private Long roomId;

    private Long roomDetailId;

    private LocalDate date;

    private int numberOfRemainingRoom;
//    public int getNumberOfRemainingRoom();

    public void decrease(){
        this.numberOfRemainingRoom -= 1;
    }

    public void increase(){
        this.numberOfRemainingRoom += 1;
    }
}

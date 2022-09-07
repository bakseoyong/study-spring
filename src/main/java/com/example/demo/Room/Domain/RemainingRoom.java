package com.example.demo.Room.Domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
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
    private Long numberOfRemainingRoom;
}

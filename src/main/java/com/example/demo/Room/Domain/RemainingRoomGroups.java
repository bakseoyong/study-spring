package com.example.demo.Room.Domain;

import javax.persistence.EntityExistsException;
import java.util.List;

public class RemainingRoomGroups {
    private List<RemainingRoom> remainingRooms;

    public RemainingRoomGroups(List<RemainingRoom> remainingRooms){
        this.remainingRooms = remainingRooms;
    }

    public RemainingRoom findByRoomId(Long roomId){
        return remainingRooms.stream()
                .filter(remainingRoom -> remainingRoom.getRoomId() == roomId)
                .findAny()
                .orElseThrow(EntityExistsException::new);
    }
}

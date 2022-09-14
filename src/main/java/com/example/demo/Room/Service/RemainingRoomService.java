package com.example.demo.Room.Service;

import com.example.demo.Room.Domain.RemainingRoom;
import com.example.demo.Room.Repository.RemainingRoomMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RemainingRoomService {
    private final RemainingRoomMongoRepository remainingRoomMongoRepository;

    @Transactional
    public Boolean isSoldOut(Long roomId, LocalDate startedAt, LocalDate endedAt){
        List<RemainingRoom> remainingRooms = this.remainingRoomMongoRepository.
                isRoomSoldOutBetweenStartedAtAndEndedAt(roomId, startedAt, endedAt);

        for(RemainingRoom remainingRoom: remainingRooms){
            if(remainingRoom.getNumberOfRemainingRoom() <= 0){
                throw new ValidationException("진행과정중 방이 매진되었습니다.");
            }
        }

        return true;
    }

    public void decreaseNumberOfRemainingRoom(Long roomId, LocalDate startedAt, LocalDate endedAt){
        List<RemainingRoom> remainingRooms = this.remainingRoomMongoRepository.
                isRoomSoldOutBetweenStartedAtAndEndedAt(roomId, startedAt, endedAt);

        for(RemainingRoom remainingRoom : remainingRooms){
            remainingRoom.setNumberOfRemainingRoom(-1);
        }

        remainingRoomMongoRepository.saveAll(remainingRooms);
    }

    public void increaseNumberOfRemainingRoom(Long roomId, LocalDate startedAt, LocalDate endedAt){
        List<RemainingRoom> remainingRooms = this.remainingRoomMongoRepository.
                isRoomSoldOutBetweenStartedAtAndEndedAt(roomId, startedAt, endedAt);

        for(RemainingRoom remainingRoom : remainingRooms){
            remainingRoom.setNumberOfRemainingRoom(1);
        }

        remainingRoomMongoRepository.saveAll(remainingRooms);
    }
}

package com.example.demo.Room.Domain;

public enum RoomStatus {
    방닫기("closed"),
    방대기("waiting"), //예약은 불가능하나, 방목록에 추가된 상태. 사용 예시) 특정 시간대에 열리는 이벤트 룸, 선착순 룸
    방열기("open");

    private String value;

    RoomStatus(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}

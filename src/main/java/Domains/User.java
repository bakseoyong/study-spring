package Domains;

import Dtos.UserRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
//NoArgsConstructor 사용하지 않는 이유 : uuid와 isOptional한 필드가 있을 수 있기 떄문에
public class User {
    private String uuid;
    private String id;
    private String password;
    private String email;
    private UserType userType;

    public User(UserRequestDto requestDto){
        this.id =
    }
}

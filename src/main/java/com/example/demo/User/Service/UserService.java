package com.example.demo.User.Service;

import com.example.demo.User.Domain.*;
import com.example.demo.User.Dto.UserSignupRequestDto;
import com.example.demo.User.Dto.UserSignupResponseDto;
import com.example.demo.User.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserSignupResponseDto save(UserSignupRequestDto userSignupRequestDto){
        User user = userSignupRequestDto.toEntity();
        return new UserSignupResponseDto(this.userRepository.save(user));
    }

    public Consumer find(Long consumerId){
        return (Consumer) userRepository.findById(consumerId)
                .orElseGet(() -> UnregisteredOrderer.toEntity());
    }

    public UnregisteredOrderer createNonConsumer(String nameStr, String phoneStr){
        Name name = Name.of(nameStr);
        Phone phone = Phone.of(phoneStr);

        UnregisteredOrderer unregisteredOrderer = UnregisteredOrderer.of(name, phone);
        userRepository.save(unregisteredOrderer);

        return unregisteredOrderer;
    }
}

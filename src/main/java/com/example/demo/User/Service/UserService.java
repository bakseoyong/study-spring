package com.example.demo.User.Service;

import com.example.demo.User.Domain.User;
import com.example.demo.User.Dto.UserSignupRequestDto;
import com.example.demo.User.Dto.UserSignupResponseDto;
import com.example.demo.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserSignupResponseDto save(UserSignupRequestDto userSignupRequestDto){
        User user = userSignupRequestDto.toEntity();
        return new UserSignupResponseDto(this.userRepository.save(user));
    }
}

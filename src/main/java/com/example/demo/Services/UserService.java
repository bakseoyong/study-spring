package com.example.demo.Services;

import com.example.demo.Domains.User;
import com.example.demo.Dtos.UserSignupRequestDto;
import com.example.demo.Dtos.UserSignupResponseDto;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserSignupResponseDto save(UserSignupRequestDto userSignupRequestDto){
        User user = userSignupRequestDto.toEntity();
        return new UserSignupResponseDto(this.userRepository.save(user));
    }
}

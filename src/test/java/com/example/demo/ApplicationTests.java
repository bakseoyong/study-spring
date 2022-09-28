package com.example.demo;

import com.example.demo.User.Domain.Department;
import com.example.demo.User.Domain.Student;
import com.example.demo.User.Domain.User;
import com.example.demo.User.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@WebAppConfiguration
@SpringBootTest
public class ApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void init(){
        User user = new User(
                "bakseoyong",
                "qwer1234",
                "test@test.com"
        );
        userRepository.save(user);
        User searched = userRepository.findAll().get(0);
        assertThat(searched.getId(), is("bakseoyong"));

        Student student = new Student(
                "bakseoyongStu",
                "qwer1234",
                "test@test.com",
                Department.컴퓨터공학과
        );
        userRepository.save(student);
        Student searchedStu = (Student) userRepository.findAll().get(1);
        assertThat(searchedStu.getId(), is("bakseoyongStu"));
    }

}

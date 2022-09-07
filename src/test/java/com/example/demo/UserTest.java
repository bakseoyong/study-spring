package com.example.demo;

import com.example.demo.User.Domain.Advertiser;
import com.example.demo.User.Domain.Department;
import com.example.demo.User.Domain.Manager;
import com.example.demo.User.Domain.Student;
import com.example.demo.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class UserTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setUp() throws Exception{
        Student student = Student.builder()
                .id("studentTest")
                .password("qwer1234")
                .email("test@test.com")
                .department(Department.컴퓨터공학과)
                .build();

        Manager manager = Manager.builder()
                .id("managerTest")
                .password("qwer1234")
                .email("test@test.com")
                .build();

        Advertiser advertiser = Advertiser.builder()
                .id("advertiserTest")
                .password("qwer1234")
                .email("test@test.com")
                .company("네이버")
                .build();

        userRepository.save(student);
        userRepository.save(manager);
        userRepository.save(advertiser);

        entityManager.clear();
    }

    @Test
    public void User_서브클래스_casting_가져오기(){
        Student student = (Student) userRepository.findAll().get(0);
        Manager manager = (Manager) userRepository.findAll().get(1);
        Advertiser advertiser = (Advertiser) userRepository.findAll().get(2);

        assertThat(student.getId(), is("studentTest"));
        assertThat(manager.getId(), is("managerTest"));
        assertThat(advertiser.getId(), is("advertiserTest"));
    }
}

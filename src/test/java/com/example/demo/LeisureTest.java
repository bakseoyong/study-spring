package com.example.demo;

import com.example.demo.Leisure.Domain.Leisure;
import com.example.demo.Leisure.Domain.LeisureCartDto;
import com.example.demo.Leisure.Domain.LeisureTicket;
import com.example.demo.Leisure.Repository.LeisureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class LeisureTest {

    @Autowired
    LeisureRepository leisureRepository;

    @BeforeEach
    public void SetUp(){
        Leisure leisure = new Leisure("레저");

        LeisureTicket leisureTicket1 = new LeisureTicket("레저티켓1");
        LeisureTicket leisureTicket2 = new LeisureTicket("레저티켓2");
        LeisureTicket leisureTicket3 = new LeisureTicket("레저티켓3");
        LeisureTicket leisureTicket4 = new LeisureTicket("레저티켓4");

        leisureTicket1.setLeisure(leisure);
        leisureTicket2.setLeisure(leisure);
        leisureTicket3.setLeisure(leisure);
        leisureTicket4.setLeisure(leisure);

        leisure.addLeisureTicket(leisureTicket1);
        leisure.addLeisureTicket(leisureTicket2);
        leisure.addLeisureTicket(leisureTicket3);
        leisure.addLeisureTicket(leisureTicket4);

        leisureRepository.save(leisure);
    }

    @Test
    public void 정상_조회_테스트(){
        Leisure leisure = leisureRepository.findAll().get(0);
        List<LeisureTicket> leisureTickets = leisure.getLeisureTickets();

        System.out.println("leisure tickets size is : " + leisureTickets.size());
    }

    @Test
    public void Repository_getCartAttrById_테스트(){
        Leisure leisure = leisureRepository.findAll().get(0);
        List<LeisureTicket> leisureTickets = leisure.getLeisureTickets();
        LeisureTicket leisureTicket = leisureTickets.get(0);
        /**
         * org.hibernate.exception.SQLGrammarException
         */
        //LeisureCartDto leisureCartDto = leisureRepository.getCartAttrById(leisureTicket.getId());
        //System.out.println(leisureCartDto.getLeisureName());
        //System.out.println(leisureCartDto.getLeisureTicketName());
    }
}

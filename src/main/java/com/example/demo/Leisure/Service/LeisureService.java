package com.example.demo.Leisure.Service;

import com.example.demo.Leisure.Domain.Leisure;
import com.example.demo.Leisure.Domain.LeisureTicket;
import com.example.demo.Leisure.Repository.LeisureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeisureService {
    @Autowired
    private LeisureRepository leisureRepository;

    public void openLeisure(){
        Leisure leisure = new Leisure("테스트 레져");

        leisureRepository.save(leisure);
    }

    public void addLeisureTicket(){
        Leisure leisure = leisureRepository.findAll().get(0);

        LeisureTicket leisureTicket = new LeisureTicket("테스트 티켓 1");
        leisure.addLeisureTicket(leisureTicket);
        leisureTicket.setLeisure(leisure);

        leisureRepository.save(leisure);
    }

}

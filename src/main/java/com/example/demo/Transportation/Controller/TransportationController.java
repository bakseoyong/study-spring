//package com.example.demo.Transportation.Controller;
//
//import com.example.demo.Transportation.Domain.BusTerminal;
//import com.example.demo.Transportation.Domain.TrainStation;
//import com.example.demo.Transportation.Dto.TicketDto;
//import com.example.demo.Transportation.Dto.TicketSearchInfo;
//import com.example.demo.Transportation.Service.BusService;
//import com.example.demo.Transportation.Service.TrainService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Controller
//public class TransportationController {
//    @Autowired
//    private TrainService trainService;
//
////    @Autowired
////    private BusService busService;
//
//    @GetMapping("/train")
//    public String trainTicketing(Model model){
//        //station은 아이디와 이름
//        List<TrainStation> trainStations = trainService.getStations();
//
//        model.addAttribute("trainStations", trainStations);
//
//        return "trainTicketing";
//    }
//
//    @GetMapping("/bus")
//    public String busTicketing(Model model){
////        List<BusTerminal> busTerminals = busService.getTerminals();
////
////        model.addAttribute("busTerminals", busTerminals);
////
////        return "busTicketing";
//    }
//
//    //Get요청으로 못 들어감.
//    @PostMapping("/train/ticket")
//    public String showTickets(Model model, @RequestBody TicketSearchInfo ticketSearchInfo){
//        String dptStationId = ticketSearchInfo.getDptId() ;
//        String arrStationId = ticketSearchInfo.getArrId();
//        LocalDate date = ticketSearchInfo.getDate();
//        Long adultNum = ticketSearchInfo.getAdultNum();
//
//        TicketDto ticketDto =
//                trainService.showTicketsByInfo(dptStationId, arrStationId, date);
//        ticketDto.setDate(date);
//        ticketDto.setAdultNum(adultNum);
//
//        model.addAttribute("ticketDto", ticketDto);
//
//        return "trainTickets";
//    }
//
//    @PostMapping("/bus/ticket")
//    public String showTickets(Model model, @RequestBody TicketSearchInfo ticketSearchInfo){
//        String dptTerminalId = ticketSearchInfo.getDptId();
//        String arrTerminalId = ticketSearchInfo.getArrId();
//        LocalDate date = ticketSearchInfo.getDate();
//        Long adultNum = ticketSearchInfo.getAdultNum();
//
//        TicketDto ticketDto =
//                trainService.showTicketsByInfo(dptTerminalId, arrTerminalId, date);
//        ticketDto.setDate(date);
//        ticketDto.setAdultNum(adultNum);
//
//        model.addAttribute("ticketDto", ticketDto);
//
//        return "trainTickets";
//    }
////    @PostMapping("/api/v1/train/save")
////    public void saveStations(){
////        trainService.saveStations();
////    }
//
//    @PostMapping("/api/v1/bus/save")
//    public void saveBusTerminals(){
//        busService.saveTerminals();
//    }
//
//    @GetMapping("/train/test")
//    public void trainTest(){
//        //return trainService.getData();
//    }
//}

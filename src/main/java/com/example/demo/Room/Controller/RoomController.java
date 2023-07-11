package com.example.demo.Room.Controller;

import com.example.demo.Room.Domain.RoomBulkEditDto;
import com.example.demo.Room.Domain.RoomCalendar;
import com.example.demo.Room.Dto.NewRoomDto;
import com.example.demo.Room.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;인

import java.time.LocalDate;
import java.util.List;

@Controller
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/ceo/room/create")
    public String createRoom(Model model){
        model.addAttribute("newRoomDto", new NewRoomDto());

        return "newRoom";
    }

    @PostMapping(value = "/api/v2/room/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
        public String saveRoom(@RequestPart(value = "newRoomDto") NewRoomDto newRoomDto,
                         @RequestPart(value = "multipartFile", required = false) MultipartFile multipartFile){
        System.out.println(newRoomDto.getName());
        System.out.println(newRoomDto.getMaximumPersonNum());
        System.out.println(newRoomDto.getStandardPersonNum());
        System.out.println(multipartFile.getOriginalFilename());

//        System.out.println(newRoomDto.getMultipartFiles());
        return "index";
    }

    @PostMapping(value = "/api/v1/room/update/벌크??") //remainingRoom과 다름.
    public String roomBulkEdit(@RequestBody RoomBulkEditDto roomBulkEditDto) {
        roomService.roomBulkEdit(roomBulkEditDto);

        return "index";
    }

//    // 이전 버전
//    public void saveRoom(@ModelAttribute("newRoomDto") NewRoomDto newRoomDto){
//        System.out.println(newRoomDto.getName());
//        System.out.println(newRoomDto.getMaximumPersonNum());
//        System.out.println(newRoomDto.getStandardPersonNum());
//        System.out.println(newRoomDto.getMultipartFiles());
//
//    }
}

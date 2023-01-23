package com.example.demo.Map.Controller;

import com.example.demo.Map.Dto.AddressSearchDto;
import com.example.demo.Map.Dto.AddressSearchResultDto;
import com.example.demo.Map.Service.MapService;
import com.example.demo.Location.Dto.LocationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class MapController {
    @Autowired
    private MapService mapService;

    @GetMapping("/map/test")
    public String mapTest(){
        return "naverMap";
    }

    @GetMapping("/map/transcoord")
    public String mapTransCoordTest(){
        return "naverMapTransCoord";
    }

    @GetMapping("/map/address/search")
    public String searchAddress(Model model, AddressSearchDto addressSearchDto){
        model.addAttribute("addressSearchDto", addressSearchDto);

        return "addressSearch";
    }

    @PostMapping("/api/v1/map/address/search")
    public String getRelatedAddress(Model model, @RequestBody AddressSearchDto addressSearchDto){
        List<AddressSearchResultDto> addressSearchResultDtos =
                mapService.getRelatedAddresses(addressSearchDto.getAddress());

        model.addAttribute("addressSearchResultDtos", addressSearchResultDtos);

        return "addressSearchList";
        //return "addressSearch::#addressSearchResultList"
    }

    @PostMapping("/api/v1/map/address/save")
    public String savePlaceAddress(@RequestBody LocationDto locationDto){
        Long placeId = 1L;

        System.out.println(locationDto.getRoadAddress());
        System.out.println(locationDto.getJibunAddress());
        System.out.println(locationDto.getTransportation());
        System.out.println(locationDto.getDirections());

//        mapService.saveLocation(placeId, locationDto);

        return "index";
    }
}

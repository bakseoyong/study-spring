package com.example.demo.Wishlist.Controller;

import com.example.demo.Wishlist.Dto.NewWishlistDto;
import com.example.demo.Wishlist.Dto.WishlistDto;
import com.example.demo.Wishlist.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @GetMapping(value = "/wishlist")
    public String test(Model model, @RequestParam String type){ // leisure, local
        WishlistDto wishlistDto = wishlistService.getWishlist(1L);
        model.addAttribute("wishlistDto", wishlistDto);

        return "myWishlist";
    }

    @PostMapping(value = "/api/v1/save/wishlist")
    public void newWishlist(@RequestBody NewWishlistDto newWishlistDto){
        wishlistService.newWishlist(1L, newWishlistDto);
    }
}

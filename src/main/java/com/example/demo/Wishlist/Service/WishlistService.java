package com.example.demo.Wishlist.Service;

import com.example.demo.Leisure.Domain.Leisure;
import com.example.demo.Leisure.Repository.LeisureRepository;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Repository.UserRepository;
import com.example.demo.Wishlist.Domain.Wishlist;
import com.example.demo.Wishlist.Dto.NewWishlistDto;
import com.example.demo.Wishlist.Dto.WishlistDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeisureRepository leisureRepository;
    @Autowired
    private PlaceRepository placeRepository;

    @Transactional
    public void newWishlist(Long consumerId, NewWishlistDto newWishlistDto){
        Consumer consumer = (Consumer) userRepository.findById(consumerId).orElseThrow(EntityNotFoundException::new);

        consumer.addWishlist(new Wishlist(newWishlistDto.getType(), newWishlistDto.getId()));

        userRepository.save(consumer);
    }

    @Transactional
    public WishlistDto getWishlist(Long consumerId){
        List<Long> leisureIds = new ArrayList<>();
        List<Long> placeIds = new ArrayList<>();

        Consumer consumer = (Consumer) userRepository.findById(consumerId).orElseThrow(EntityNotFoundException::new);

        List<Wishlist> wishlists = consumer.getWishlists();

        for(Wishlist wishlist: wishlists){
            String type = wishlist.getType();
            if(type.equals("leisure")){
                leisureIds.add(wishlist.getItemId());
            }else if(type.equals("local")){
                placeIds.add(wishlist.getItemId());
            }
        }

        List<Leisure> leisures = leisureRepository.findAllById(leisureIds);
        List<Place> places = placeRepository.findAllById(placeIds);

        return WishlistDto.builder().leisures(leisures).places(places).build();
    }
}

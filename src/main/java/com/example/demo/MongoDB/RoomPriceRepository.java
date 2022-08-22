package com.example.demo.MongoDB;

import com.example.demo.Domains.RoomPrice;
import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Repository;

//@Repository
public interface RoomPriceRepository extends MongoRepository<RoomPrice, String> { }

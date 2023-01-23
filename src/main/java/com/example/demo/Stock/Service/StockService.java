package com.example.demo.Stock.Service;

import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.Room.Repository.RoomDetailRepository;
import com.example.demo.Stock.Domain.RoomDetailStock;
import com.example.demo.Stock.Repository.RoomDetailStockRepository;
import com.example.demo.Stock.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {
    private final RoomDetailRepository roomDetailRepository;
    private final StockRepository stockRepository;
    private final RoomDetailStockRepository roomDetailStockRepository;

    public void create(Long roomDetailId, LocalDate start, LocalDate end, Long total){
        RoomDetail roomDetail = roomDetailRepository.findById(roomDetailId)
                .orElseThrow(EntityNotFoundException::new);

        List<RoomDetailStock> already = roomDetailStockRepository.findAlreadyStocks(roomDetailId, start, end);

        Map<LocalDate, RoomDetailStock> search = already.stream()
                .collect(Collectors.toMap(RoomDetailStock::getDate, Function.identity()));

        List<RoomDetailStock> roomDetailStocks = new ArrayList<>();

        for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            RoomDetailStock roomDetailStock = search.get(date);
            if(roomDetailStock == null) {
                roomDetailStocks.add(RoomDetailStock.of(roomDetail, date, total));
            }else{
                roomDetailStock.update(total);
            }
        }

        roomDetailStockRepository.saveAll(roomDetailStocks);
    }
}

package com.example.demo.Recommend.Service;

import com.example.demo.Leisure.Repository.LeisureRepository;
import com.example.demo.Recommend.Dto.RecommendPropertyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeisureRecommendService implements RecommendService {
    @Autowired
    private LeisureRepository leisureRepository;
    @Override
    public List<RecommendPropertyDto> showRelatedRecommendProperties(Long propertyId) {
        return null;
    }


}

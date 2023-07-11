package com.example.demo.Recommend.Service;

import com.example.demo.Recommend.Dto.RecommendPropertyDto;

import java.util.List;


public interface RecommendService {

    public List<RecommendPropertyDto> showRelatedRecommendProperties(Long propertyId);
}

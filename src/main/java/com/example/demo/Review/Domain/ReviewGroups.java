package com.example.demo.Review.Domain;

import com.example.demo.Review.Dto.NewReviewDto;
import com.example.demo.Review.Dto.ReviewDto;
import com.example.demo.utils.Converter.ImagePathConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewGroups {
    private List<Review> reviews;

    public ReviewGroups(List<Review> reviews){
        this.reviews = reviews;
    }

    //모든 점수 다 계산해서 주자 => 개별로만 필요하지 않음.
    public Map<String, Double> getRating(){
        HashMap<String, Double> result = new HashMap<>();

        Double overall = 0.0;
        Double service = 0.0;
        Double cleanliness = 0.0;
        Double convenience = 0.0;
        Double satisfaction = 0.0;

        for(Review review: reviews){
            overall += review.getOverall();
            service += review.getService();
            cleanliness += review.getCleanliness();
            convenience += review.getConvenience();
            satisfaction += review.getSatisfaction();
        }

        result.put("overall", Math.round(overall / reviews.size() * 10) / 10.0);
        result.put("service", Math.round(service / reviews.size() * 10) / 10.0);
        result.put("cleanliness", Math.round(cleanliness / reviews.size() * 10) / 10.0);
        result.put("convenience", Math.round(convenience / reviews.size() * 10) / 10.0);
        result.put("satisfaction", Math.round(satisfaction / reviews.size() * 10) / 10.0);

        return result;
    }


    public List<ReviewDto> toReviewDto(){
        ImagePathConverter imagePathConverter = new ImagePathConverter();
        List<ReviewDto> reviewDtos = new ArrayList<>();

        for(Review review: reviews) {
            List<String> imagePaths;

            //imagePaths가 null일때 책임 : toReviewDto vs converter
            //converter에게 책임을 전가하려면 convertToEntityAttribute()메서드의 응집도가 낮아짐
            //결론 : toReviewDto
            if(review.getImagePaths() != null){
                imagePaths = imagePathConverter.convertToEntityAttribute(review.getImagePaths());
            }else{
                imagePaths = null;
            }

            reviewDtos.add(
                    ReviewDto.builder()
                    .roomId(review.getRoom().getId())
                    .roomName(review.getRoom().getName())
                    .imagePaths(imagePaths)
                    .consumerId(review.getConsumer().getId())
                    .nickname(review.getConsumer().getNickname())
                    .content(review.getContent())
                    .overall(review.getOverall())
                    .answer(review.getAnswer())
                    .writtenAt(review.getWrittenAt())
                    .build()
            );
        }
        return reviewDtos;
    }

}

package com.example.demo.Review.Controller;

import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Domain.PlaceType;
import com.example.demo.Review.Domain.BestReview;
import com.example.demo.Review.Domain.Review;
import com.example.demo.Review.Dto.BestReviewRedirectDto;
import com.example.demo.Review.Dto.NewReviewDto;
import com.example.demo.Review.Dto.ReviewDto;
import com.example.demo.Review.Dto.TestDto;
import com.example.demo.Review.Service.ReviewService;
import com.example.demo.Room.Domain.Room;
import com.example.demo.User.Domain.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/review/create")
    public String newReview(Model model){
        Long originalPrice = 100000L;
        Long point = Math.round(originalPrice * 0.0015);
        model.addAttribute("expectPoint", point);
        model.addAttribute("newReviewDto", new NewReviewDto());

        return "newReview";
    }

    //야놀자에서는 place에 대한걸 전부 가져온다음에 특정 룸만 바랄때 그때 필터링 하나보다.
    //place에서 바로 보여주는게 베스트지만
    //review Service단에서 테스트 해보자
    //1단계 : placeId를 주면 거기에 맞는 리뷰들을 전부 꺼내오기
    @GetMapping("/review/{placeId}")
    public String showReviews(@PathVariable("placeId") Long placeId,  Model model){
        System.out.println(placeId);


        List<ReviewDto> reviewDtos = reviewService.getReviews(placeId);

        model.addAttribute("reviewDtos", reviewDtos);

        return "reviews";
    }

    @PostMapping(value = "/api/v1/review/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String saveReview(@ModelAttribute NewReviewDto newReviewDto,
                @RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFiles){

        //테스트용 consumerId (원래 세션, jwt 토큰 등의 방법을 이용해서 받아야 한다.)
        Long consumerId = 1L;

        reviewService.createReview(newReviewDto, multipartFiles, consumerId);

        return "index";
    }

    @GetMapping(value = "/ceo/review")
    public String newlyReviews(Model model){
        //테스트용 placeId는 1이다. 추후에 사장님 인증(세션, 쿠키, 토큰)등을 통해 placeId를 알아올 수 있다.
        //결과물을 만드는게 더 필요한 순간이다. service layer에 room과 review집어넣고 프론트단에서 보여주기까지 고고
        Long placeId = 1L;
        List<ReviewDto> reviewDtos = reviewService.getNewlyReviews(placeId);

        model.addAttribute("reviewDtos", reviewDtos);

        return "myBusinessReview";
    }

    @PostMapping(value = "/ceo/review/test")
    public String newlyReviewsPostman(Model model, @RequestBody List<ReviewDto> reviewDtos){

        System.out.println(reviewDtos.size());
        System.out.println(reviewDtos.get(0).getNickname());

        //한개지만 일단 테스트용으로 list
        model.addAttribute("reviewDtos", reviewDtos);

        return "myBusinessReview";
    }

    @PostMapping(value = "/api/v1/review/best")
    public String bestReviewSetting(Model model, @RequestBody ReviewDto reviewDto){
        //3개면 페이지 들어가서 기존의 베스트 리뷰와 변경
        //3개 미만이면 바로 적용

        //컨트롤러는 서비스 계층에서 메시지를 전송하는데, 컨트롤러 입장에서 가장 중요한 생각은
        // 클라이언트의 요청을 어디로 보내줘야 할까.
        //=> 메서드 명 : getAlreadyBestReviews -> determineRedirectPath

        //새로운 dto로 받아야 된다고 생각한다.
        //기존의 reviewDto를 받으면 컨트롤러는 해당 사이즈의 크기를 보고 판단을 해야 한다.
        //새로운 dto값 안에 boolean값을 집어넣으면 controller가 3개이상인지 미만인지 확인할 필요없이 받은 boolean값 대로 처리한다.
        //=> 메서드 명 : determineRedirectPath -> isRedirectToBestReviewChangePath

        //List<ReviewDto> alreadyBestReviewDtos = reviewService.getAlreadyBestReviews(reviewDto.getPlaceId());

        /**
         * 어떻게 테스트 해야 될 지 고민하느라 너무 많은 시간 소모, 정답 못찾음
         * => 일단 미루고 실행이 되는걸 우선으로 해보자
         * BestReviewRedirectDto bestReviewRedirectDto = reviewService.isRedirectToBestReviewChangePath(reviewDto);
         */

        //이렇게 만들고 postman으로 테스트
        BestReviewRedirectDto bestReviewRedirectDto = new BestReviewRedirectDto();
        bestReviewRedirectDto.setIsRedirect(true);
        Consumer consumer = Consumer.builder()
                .loginId("id")
                .email("test@test")
                .password("password")
                .build();
        //place, room도 추가해야 dto할때 nullPointerException이 발생하지 않음.
        Place place = Place.builder().placeType(PlaceType.펜션).name("테스트펜션").address("테스트주소").build();
        Room room1 = Room.builder().place(place).name("테스트룸").build();

        Review review1 = Review.builder().room(room1).consumer(consumer).overall(1.0).service(2.0).convenience(3.0).cleanliness(4.0).satisfaction(5.0).content("안녕하세요").build();
        Review review2 = Review.builder().room(room1).consumer(consumer).overall(1.0).service(2.0).convenience(3.0).cleanliness(4.0).satisfaction(5.0).content("안녕하세요").build();
        Review review3 = Review.builder().room(room1).consumer(consumer).overall(1.0).service(2.0).convenience(3.0).cleanliness(4.0).satisfaction(5.0).content("안녕하세요").build();
        List<ReviewDto> reviewDtos = Arrays.asList(review1.toDto(), review2.toDto(), review3.toDto());
        bestReviewRedirectDto.setBestReviews(reviewDtos);

        if(bestReviewRedirectDto.getIsRedirect()){
            model.addAttribute("alreadyBestReviews", bestReviewRedirectDto.getBestReviews());
            model.addAttribute("reviewDto", reviewDto);
            return "myBusinessBestReview";
        }else{
            return "index";
        }
    }

    //test version 2 , json list를 성공적으로 받기
    //결론 : postman에서 json만 보낼때는 raw - json 형식으로 보내야 한다.
    // 기존에 multipartFile 테스트를 위해 form-data로 설정하니까 계속 오류가 발생.
    // + 기존에는 @ModelAttribute 로 dto list를 받았다. 이유 : requestbody + model.addAttribute 합한 기능인줄 알았는데 오류 발생
    // @RequestBody로 설정해야 오류가 안났다.
    @PostMapping(value = "/ceo/review/test2")
    public String newlyReviewsTest2(Model model, @RequestBody List<TestDto> reviewDtos){

        System.out.println(reviewDtos.size());

        //한개지만 일단 테스트용으로 list
        model.addAttribute("reviewDtos", reviewDtos);

        return "myBusinessReview";
    }

    @PutMapping(value = "/api/v1/review/best/change")
    public String convertBestReview(@RequestParam("reviewId") Long reviewId,
                                    @RequestParam("bestReviewId") Long bestReviewId){
        System.out.println("review id is : " + reviewId);
        System.out.println("best review id is : " + bestReviewId); //정상적으로 들어온다.

        //기존 리뷰는 다운캐스팅
        //새로운 리뷰는 업캐스팅
        reviewService.convertGeneralAndBest(reviewId, bestReviewId);

        return "index";
    }

}

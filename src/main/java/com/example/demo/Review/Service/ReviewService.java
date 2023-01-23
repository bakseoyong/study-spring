package com.example.demo.Review.Service;

import com.example.demo.Image.Domain.ImageType;
import com.example.demo.Image.Service.ImageService;
import com.example.demo.Reservation.Repository.ReservationRepository;
import com.example.demo.Review.Domain.BestReview;
import com.example.demo.Review.Domain.Review;
import com.example.demo.Review.Domain.ReviewGroups;
import com.example.demo.Review.Dto.BestReviewRedirectDto;
import com.example.demo.Review.Dto.NewReviewDto;
import com.example.demo.Review.Dto.ReviewAnswerDto;
import com.example.demo.Review.Dto.ReviewDto;
import com.example.demo.Review.Repository.BestReviewRepository;
import com.example.demo.Review.Repository.ReviewRepository;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Repository.RoomRepository;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @Value("${images.path.absolute}")
    String absolutePath;
    @Autowired
    private ImageService imageService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BestReviewRepository bestReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;
//    @Autowired
//    ReviewPolicy reviewPolicy;



    /**
     * 1. 여러 리포지토리에 접근하는것 보다 하나의 쿼리를 만들어 한번만 접속하면 더 성능이 좋지 않을까 하는 생각에 구현한 메서드
     * 2. 영속성 컨텍스트가 잘 동작하는지 확인하기
     * 3. 유튜브 영상 올리기 좋겠다.
     *
     * => 결론 : insert문을 커스텀 쿼리를 만들어 사용하면 JPA의 장점을 활용할 수 없다.
     */
    @Transactional
    public void createReviewPersistenceContextTest(NewReviewDto newReviewDto, Long consumerId){
        Long roomId = newReviewDto.getRoomId();

        Double overall = newReviewDto.getOverall();
        Double satisfaction = newReviewDto.getSatisfaction();
        Double cleanliness = newReviewDto.getCleanliness();
        Double convenience = newReviewDto.getConvenience();
        Double service = newReviewDto.getService();
        String content = newReviewDto.getContent();

        /**
         * 1. 이렇게 저장하면 id는 자동으로 할당될까?
         */
//        reviewRepository.saveReviewAndMappings(consumerId, roomId, overall, satisfaction, cleanliness,
//                convenience, service, content);
    }

    /**
     * 테스트 코드 통과하면 createReview로 메서드명 변경하고
     * 이전껄 예전 버전으로 명시
     */
    @Transactional
    public void createReview(NewReviewDto newReviewDto, List<MultipartFile> multipartFiles, Long consumerId){
        Consumer consumer = (Consumer) userRepository.findById(consumerId).orElseThrow(EntityNotFoundException::new);
        Room room = roomRepository.findById(newReviewDto.getRoomId()).orElseThrow(EntityNotFoundException::new);

        Review review = Review.of(newReviewDto);
        review.setConsumer(consumer);
        review.setRoom(room);

        if(multipartFiles != null && !multipartFiles.isEmpty()) {
            //세 객체간의 결합도가 너무 높다.
            //이미지 서비스 객체에게 경로를 가져와달라고 요청한다. 이미지 서비스는 경로를 가져와 준다.
            //변환기 객체에게 짧은 경로를 가져와 달라고 요청한다. + 변환기 객체에게 데이터베이스 컬럼으로 변경해 달라고 요청한다.
            // => 리뷰 객체가 너무 많은 정보를 알고 있다.

            //변환기에게 절대경로리스트를 보내면 변환기가 알아서 짧은 경로로 만들어주고, 저장소에 저장한다.
            // => 결합도 감소. 하지만 결합도가 아직 남아있다.

            //리뷰 객체가 이미지 변환 객체에게 어떤걸 메시지를 요청해야 할 지 알까??
            //=> 알고 있다는 사실이 결합도를 높이는 요인

            //결론 : imagePathConverter를 imageService 비즈니스 로직 안에 집어넣기.
            //결론2 : 리뷰 서비스 객체는 이미지 경로를 달라고 요청할 수 있다. 받아서 review.setImagePaths()안에 집어넣어야 하니까.
            String imagePaths = imageService.getImagePaths(multipartFiles, ImageType.Review);

            review.setImagePaths(imagePaths);
        }

        reviewRepository.save(review);
    }

    @Transactional
    public List<ReviewDto> getReviews(Long placeId){
        List<Review> reviews = reviewRepository.findByPlaceId(placeId);
        ReviewGroups reviewGroups = new ReviewGroups(reviews);
        return reviewGroups.toReviewDto();
    }

    @Transactional
    public List<ReviewDto> getNewlyReviews(Long placeId){
        List<BestReview> bestReviews = bestReviewRepository.findBestReviewsByPlaceIdTest(placeId);
        List<Review> reviews = reviewRepository.findByPlaceIdOrderByWrittenAt(placeId);

        reviews.addAll(0, bestReviews);

        ReviewGroups reviewGroups = new ReviewGroups(reviews);
        return reviewGroups.toReviewDto();
    }

    @Transactional
    public List<ReviewDto> getHighRatingReviews(Long placeId){
        List<Review> reviews = reviewRepository.findByPlaceIdOrderByOverallDescAndWrittenAt(placeId);

        ReviewGroups reviewGroups = new ReviewGroups(reviews);
        return reviewGroups.toReviewDto();
    }

    @Transactional
    public List<ReviewDto> getLowerRatingReview(Long placeId){
        List<Review> reviews = reviewRepository.findByPlaceIdOrderByOverallAscAndWrittenAt(placeId);

        ReviewGroups reviewGroups = new ReviewGroups(reviews);
        return reviewGroups.toReviewDto();
    }

    @Transactional
    public List<ReviewDto> getAlreadyBestReviews(Long placeId){
        List<Review> bestReviews = bestReviewRepository.findBestReviewsByPlaceId(placeId);
        ReviewGroups reviewGroups = new ReviewGroups(bestReviews);
        return reviewGroups.toReviewDto();
    }

    @Transactional
    public void setBestReview(Long reviewId){
        //convertGeneralAndBest 와 코드 중복되는 부분을 해당 메소드로 합쳤음.
        //여기에서 정책들을 고려하면 될 것 같다. => 인덱스 도입

        reviewRepository.updateBestToGeneralByBestReviewId(reviewId);
    }

    @Transactional
    public BestReviewRedirectDto isRedirectToBestReviewChangePath(ReviewDto reviewDto){
        BestReviewRedirectDto bestReviewRedirectDto = new BestReviewRedirectDto();
        List<ReviewDto> bestReviews = getAlreadyBestReviews(reviewDto.getPlaceId());

        if(bestReviews.size() < 3){
            bestReviewRedirectDto.setIsRedirect(false);
            bestReviewRedirectDto.setBestReviews(null);
            setBestReview(reviewDto.getReviewId());
        }else{
            bestReviewRedirectDto.setIsRedirect(true);
            bestReviewRedirectDto.setBestReviews(bestReviews);
        }

        return bestReviewRedirectDto;
    }

    @Transactional
    public void convertGeneralAndBest(Long reviewId, Long bestReviewId){
        //reviewRepository.updateGeneralToBestByReviewId(reviewId);
        setBestReview(reviewId);
        reviewRepository.updateBestToGeneralByBestReviewId(bestReviewId);
    }

    @Transactional
    public void setAnswer(ReviewAnswerDto reviewAnswerDto){
        //General or Best Review
        Review review = reviewRepository.findById(reviewAnswerDto.getReviewId()).orElseThrow(EntityNotFoundException::new);

        review.setAnswer(review.getAnswer());
        reviewRepository.save(review);
    }

//    public void create(Long reservationId, Long score, String content, MultipartFile[] files){
//        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(EntityExistsException::new);
//        User guest = reservation.getGuest();
//        reviewPolicy.isCreatable(reservation, guest, reservation.getRoom().getPlace());
//
//        //isCreatable에서 유저인지 검증
//        Consumer consumer = (Consumer) reservation.getGuest();
//
//        //여기에 이미지까지 추가해야지
//        Review review = Review.builder()
//                .room(reservation.getRoom())
//                .consumer(consumer)
//                .score(score)
//                .content(content)
//                .build();
//
//        em.persist(review);
//        em.flush();
//
//        String url = "";
//        if(file.isEmpty()){
//            //none.image를 추가하기도 하나보다.
//        }else{
//            //review_reviewId_원본이름
//            url += "review";
//            url += review.getId();
//            url += file.getOriginalFilename();
//        }
//
//        if(!file.isEmpty()){
//            //appConfig파일에 환경변수 서정하고 gitignore
//            File dest = new File(path + url);
//            try{
//                file.transferTo(dest);
//            }catch (IllegalStateException e){
//                throw new IllegalStateException("1");
//            }catch (IOException e){
//                throw new IOException;
//            }
//        }
//    }

}

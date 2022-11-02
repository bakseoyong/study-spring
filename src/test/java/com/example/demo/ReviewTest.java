/**
 * 1. ReviewRepository가 placeId에 맞게 review들을 잘 가져와 줄 수 있는가
 * 2. 받은 리뷰들이 Groups에서 잘 처리되는가
 * 3. dto로 잘 옮길 수 있는가
 * 4. 컨트롤러로 전달
 */


package com.example.demo;

import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Domain.PlaceType;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.Review.Controller.ReviewController;
import com.example.demo.Review.Domain.BestReview;
import com.example.demo.Review.Dto.NewReviewDto;
import com.example.demo.Review.Dto.ReviewDto;
import com.example.demo.Review.Repository.BestReviewRepository;
import com.example.demo.Review.Repository.ReviewRepository;
import com.example.demo.Review.Service.ReviewService;
import com.example.demo.Room.Repository.RoomRepository;
import com.example.demo.Room.Service.RoomService;
import com.example.demo.User.Domain.User;
import com.example.demo.User.Repository.UserRepository;
import com.example.demo.Review.Domain.Review;
import com.example.demo.Place.Domain.FacilitiesService;
import com.example.demo.Room.Domain.Room;
import com.example.demo.User.Domain.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@Transactional
public class ReviewTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BestReviewRepository bestReviewRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewController reviewController;

    @BeforeEach
    public void newSetUp(){
        Consumer consumer = Consumer.builder()
                .loginId("id")
                .email("test@test")
                .password("password")
                .build();
        userRepository.save(consumer);

        Place place = Place.builder().placeType(PlaceType.펜션).name("테스트펜션").address("테스트주소").build();

        Room room1 = Room.builder().name("테스트룸").build();
        Room room2 = Room.builder().name("테스트룸").build();

        Review review1 = Review.builder().consumer(consumer).overall(1.0).service(2.0).convenience(3.0).cleanliness(4.0).satisfaction(5.0).content("안녕하세요").build();
        Review review2 = Review.builder().consumer(consumer).overall(1.0).service(2.0).convenience(3.0).cleanliness(4.0).satisfaction(5.0).content("안녕하세요").build();
        Review review3 = Review.builder().consumer(consumer).overall(1.0).service(2.0).convenience(3.0).cleanliness(4.0).satisfaction(5.0).content("안녕하세요").build();
        Review review4 = Review.builder().consumer(consumer).overall(1.0).service(2.0).convenience(3.0).cleanliness(4.0).satisfaction(5.0).content("안녕하세요").build();
        BestReview bestReview1 = new BestReview(1.0, 2.0, 3.0, 4.0, 5.0, "안녕하세요", null, consumer, room1);
        BestReview bestReview2 = new BestReview(1.0, 2.0, 3.0, 4.0, 5.0, "안녕하세요", null, consumer, room1);
        BestReview bestReview3 = new BestReview(1.0, 2.0, 3.0, 4.0, 5.0, "안녕하세요", null, consumer, room1);

        //주인이 아닌 곳의 입력된 값은 외래키에 영향을 주지 않음.
//        room1.addReview(review1);
//        room1.addReview(review2);
//        room2.addReview(review3);
//        room2.addReview(review4);
//
//        place.addRoom(room1);
//        place.addRoom(room2);
//
//        entityManager.persist(room1);
//        entityManager.persist(room2);
//        entityManager.persist(place);



        //STep 2.

//        review1.setRoom(room1);
//        review2.setRoom(room1);
//        review3.setRoom(room2);
//        review4.setRoom(room2);
//
//        room1.setPlace(place);
//        room2.setPlace(place);

//        entityManager.persist(review1);
//        entityManager.persist(review2);
//        entityManager.persist(review3);
//        entityManager.persist(review4);
//        //review 1~4만 persist했을 경우 trasient 관련 오류가 발생했는데 아래에 3개 추가하니까 해당 오류는 사라졌다.
//        //대신 place 안에 room이 존재하지 않는다.
//        entityManager.persist(room1);
//        entityManager.persist(room2);
//        entityManager.persist(place);

        //Step 3. persist 순서가 중요한 것 같다.
        //place -> room -> review
        entityManager.persist(place);
        place.addRoom(room1);
        room1.setPlace(place);
        place.addRoom(room2);
        room2.setPlace(place);
        entityManager.persist(room1);
        entityManager.persist(room2);
        room1.addReview(review1);
        room1.addReview(review2);
        room2.addReview(review3);
        room2.addReview(review4);
        room1.addReview(bestReview1);
        room1.addReview(bestReview2);
        room1.addReview(bestReview3);
        review1.setRoom(room1);
        review2.setRoom(room1);
        review3.setRoom(room2);
        review4.setRoom(room2);
        bestReview1.setRoom(room1);
        bestReview2.setRoom(room1);
        bestReview3.setRoom(room1);
        entityManager.persist(review1);
        entityManager.persist(review2);
        entityManager.persist(review3);
        entityManager.persist(review4);
        entityManager.persist(bestReview1);
        entityManager.persist(bestReview2);
        entityManager.persist(bestReview3);
    }

    @AfterEach
    public void afterEach(){
        placeRepository.deleteAll();
    }

    @Test
    public void 관련정보(){
        /**
         * 결론: 양방향 매핑시 연관관계의 주인에 값을 입력해야 한다.
         * 순수한 객체 관계를 고려하면 양쪽 다 값을 입력해야 한다.
         *
         * team.getMembers().add(member);
         * member.setTeam(team); //연관관계의 주인에 값 입력
         * em.persist(member);
         *
         * Q. 만약 여기서 team.getMembers().add(member); 을 넣지 않는다면 어떤 문제가 있을까?
         * A. DB에 반영하는데 문제는 생기지 않는다. 하지만, 영속화 컨텍스트의 1차 캐시에 저장된 team에서는 members에 해당 Member가 추가되지 않은 상태이다.
         * 이런 상황에서 team.members를 사용하게 된다면 DB에서 조회하는게 아닌 1차 캐시에서 꺼내 사용하기 때문에 해당 member가 추가되지 않은 결과가 반환 될 것이고, 문제가 생기게 된다.
         * 그렇기 때문에 양쪽에 모두 값을 세팅해주는게 맞다.
         */
    }

    @Test
    public void JPA_연관관계_연습(){
        List<Place> places = placeRepository.findAll();
        System.out.println("place size is : " + places.size());

        //이 메서드는 동작하면서 편함.
        List<Room> rooms = places.get(0).getRooms();
        for(Room room: rooms){
            System.out.println("room id is : " + room.getId());
            //System.out.println("room's place id is : " + room.getPlace().getId());
        }
        System.out.println("place.getRooms() size is : " + places.get(0).getRooms().size());

        // 아래 메서드는 동작하지 않고 복잡함
        Long placeId = places.get(0).getId();
        System.out.println("place id is : " + placeId);
        //review Repository에서 room 리스트를 꺼내려고 시도해서 converting 오류가 발생하는 것 같다.
        //내용 그대로 room Repository로 변경해보자
        //convert 오류가 더 구체적인 내용으로 변경되었다.
        //결론 : 리포지토리의 위치에 상관없이 test_findRoomByPlaceId는 select로 아이디만을 원했는데(r.id) 그걸 room list에 집어넣으려니까
        //생긴 오류였다.
        List<Long> roomIds = reviewRepository.test_findRoomByPlaceId(places.get(0).getId());
        List<Review> reviews = reviewRepository.test2_findReviewByRoomId(roomIds);
        //성공!!!
        System.out.println("review Repository : test2() review size is : " + reviews.size());
    }

    @Test
    public void 오버로딩_중복문제_개선메서드_정상동작_테스트(){
        NewReviewDto soonReview5 = new NewReviewDto();

        Consumer consumer = (Consumer) userRepository.findAll().get(0);
        Room room = roomRepository.findAll().get(0);
        Place place = room.getPlace();

        soonReview5.setRoomId(room.getId());
        soonReview5.setOverall(1.0);
        soonReview5.setService(2.0);
        soonReview5.setCleanliness(3.0);
        soonReview5.setConvenience(4.0);
        soonReview5.setSatisfaction(5.0);
        soonReview5.setContent("너무좋아용");

        reviewService.createReview(soonReview5, null, consumer.getId());
    }

    @Test
    public void 리뷰_생성_테스트_덧붙여서_최근_리뷰_확인까지(){
        //place, room, review까지 beforeEach에서 저장된 상황
        //새로운 리뷰 review 5를 저장하는 경우
        NewReviewDto soonReview5 = new NewReviewDto();

        Consumer consumer = (Consumer) userRepository.findAll().get(0);
        Room room = roomRepository.findAll().get(0);
        Place place = room.getPlace();

        soonReview5.setRoomId(room.getId());
        soonReview5.setOverall(1.0);
        soonReview5.setService(2.0);
        soonReview5.setCleanliness(3.0);
        soonReview5.setConvenience(4.0);
        soonReview5.setSatisfaction(5.0);
        soonReview5.setContent("너무좋아용");

        reviewService.createReview(soonReview5, null, consumer.getId());

        //잘 들어갔는지 확인하기(완)
        //Error 1. 이미지가 없는데 getNewlyReviews에는 이미지 converter를 통해 List화 시키는 코드가 있음(완)
        //Additional 1. Converter가 절대경로를 전부 저장하면 중복되는 경로가 많다. images까지는 저장하지 말고 그 하위폴더부터 저장하도록 하자.(완)
        //  ㄴ 절대경로를 함축시키는 책임을 지는 객체는 누가 될까. 기존 메서드에게 파일명까지 줄어주라는 것은 메서드안에 너무 많은 역할을 주어지게 한다.(완)
        //  ㄴ Converter에서 줄이도록 하자.(완)
        //  ㄴ html image src 코드를 전부 변경해 줘야 한다.
        //Additional 2. createReview 중복 제거하자. 현재 오버로딩으로 2개의 메서드를 가지고 있는데 변경에 있어 굉장히 신경쓰인다.(완)
        //  ㄴ createReviewTotal 메서드 테스트하기.(완)
        //Error 2. 기존 Review들이 consumer를 설정하지 않아 Dto화 할떄 NullPointerException발생.(완)
        List<ReviewDto> reviewDtos = reviewService.getNewlyReviews(place.getId());
        System.out.println("reviewDtos size is : " + reviewDtos.size());

    }

    @Test
    public void 최근_작성된_리뷰순으로_불러오기_테스트(){

        /**
         *            TimeUnit.SECONDS.sleep(1L); 추가해주고 Interrupt Exception 처리해 주면 된다.
         */

        Place place = placeRepository.findAll().get(0);
        Long placeId = place.getId();

        System.out.println("place Id is : " + placeId);
        List<Review> reviews = reviewRepository.findByPlaceIdOrderByWrittenDateLimitFive(placeId);
        System.out.println("reviews num is : " + reviews.size());

        System.out.println("Is reviews order by writtenAt desc ??? ");
        for(Review review: reviews){
            System.out.println(review.getWrittenAt());
        }
    }

    @Test
    public void 기존_베스트_리뷰들_불러오기(){
        Place place = placeRepository.findAll().get(0);
        System.out.println("place id is : " + place.getId());

        List<ReviewDto> bestReviews = reviewService.getAlreadyBestReviews(place.getId());
        System.out.println("best reviews size is : " + bestReviews.size());
    }

    @Test
    public void native_query_테스트(){
        //@Transactional 사용해서 cannot find resultset 오류 해결
        List<Review> reviews = reviewRepository.nativeQueryTest1();
        System.out.println("reviews size is : " + reviews.size());
    }

    @Test // 해결 못 함
    public void repository_changeReviewGeneralToBest_테스트(){
        Place place = placeRepository.findAll().get(0);
        Review review = reviewRepository.findAll().get(0); //General Review

        System.out.println("review class is : " + review.getClass());

        reviewRepository.updateBestToGeneralByBestReviewId(review.getId());

        List<BestReview> bestReviews = bestReviewRepository.findAll();

        System.out.println("best review size is : " + bestReviews.size()); // 4
    }

    @Test
    public void ReviewRepository_deleteById_테스트(){
        System.out.println("before reivews size is : " + reviewRepository.findAll().size());
        Review review = reviewRepository.findAll().get(0);
        //reviewRepository.delete(review);
        reviewRepository.deleteById(review.getId());

        //@PreRemove 설정 후 정상 동작!!
        System.out.println("after reviews size is : " + reviewRepository.findAll().size());
    }

    @Test
    public void 일반_리뷰만_조회하기_테스트(){
        List<Review> reviews = reviewRepository.findAllByGeneralType();
        List<BestReview> bestReviews = bestReviewRepository.findAll();

        System.out.println(reviews.size()); // 4
        System.out.println(bestReviews.size()); // 3
    }

    @Test
    public void 서비스_convertGeneralAndBest_테스트(){
        List<Review> reviews = reviewRepository.findAllByGeneralType();
        List<BestReview> bestReviews = bestReviewRepository.findAll();

        Review review = reviews.get(0);
        BestReview bestReview = bestReviews.get(0);

        System.out.println("first review id is : " + review.getId()); // 4
        System.out.println("first best review id is : " + bestReview.getId()); // 8

        reviewRepository.updateBestToGeneralByBestReviewId(bestReview.getId());
        reviewRepository.updateGeneralToBestByReviewId(review.getId());

        List<Review> afterReviews = reviewRepository.findAllByGeneralType(); // 8
        List<BestReview> afterBestReviews = bestReviewRepository.findAll(); // 4

        System.out.println("last review id is : " + afterReviews.get(afterReviews.size() - 1).getId());
        System.out.println("first best review id is : " + afterBestReviews.get(0).getId());
    }

    @Test
    public void 기존_베스트_리뷰가_3개_미만일때_새로운_리뷰를_베스트_리뷰로_전환(){
        Place place = placeRepository.findAll().get(0);

        //기존 베스트 리뷰 삭제 => 기존 베스트 리뷰를 일반 리뷰로 변경
        //List<Review> reviews = reviewRepository.findAll();
        List<Review> reviews = bestReviewRepository.findBestReviewsByPlaceId(place.getId());
        BestReview bestReview = (BestReview) reviews.get(0);
        System.out.println("soon review class is : " + bestReview.getClass()); //BestReview 맞음
        reviewRepository.updateBestToGeneralByBestReviewId(bestReview.getId());
        List<Review> afterReviews = bestReviewRepository.findBestReviewsByPlaceId(place.getId());
        System.out.println("convert review class is : " + afterReviews.size()); //size 값이 2로 변경
//        System.out.println("before total review size is : " + reviewRepository.findAll().size());
//
//        reviewRepository.save(review);
//        //deleteById가 작동하지 않음 - 연관 관계 때문에 - 리뷰가 방의 주인관계 - 해결
//        reviewRepository.deleteById(soonConvertToGeneralReview.getId());
//
//        System.out.println("after total review size is : " + reviewRepository.findAll().size());
//        System.out.println("best review size is : " + reviewService.getAlreadyBestReviews(place.getId()).size());

//
//        //새로운 리뷰 한개를 베스트 리뷰로 올리기
//        Review soonBestReview = Review.builder().overall(1.0).service(2.0).convenience(3.0).cleanliness(4.0).satisfaction(5.0).content("안녕하세요").build();
//        reviewRepository.save(soonBestReview);
//
//        //삭제 되었는지 확인
//        List<ReviewDto> bestReviews = reviewService.getAlreadyBestReviews(place.getId());
//        System.out.println("best reviews size is : " + bestReviews.size());
//
//
//        reviewService.setBestReview(soonBestReview.getId());
//        //setBestReview의 비즈니스 로직으로 새로운 엔티티가 새로 생긴건지, 기존의 엔티티가 업데이트 된건지 확인
//        List<Review> afterUpdateReviews = reviewRepository.findAll();
//        System.out.println("after update, total reviews size is : " + afterUpdateReviews.size());

    }

//    @BeforeEach
//    public void setUp(){
//        Consumer consumer = Consumer.builder()
//                .id("consumerTest")
//                .password("qwer1234")
//                .email("test@test.com")
//                .build();
//
//        userRepository.save(consumer);
//
//        Place place = Place.builder()
//                .businessType(PlaceType.호텔)
//                .name("테스트호텔")
//                .address("테스트광역시 테스트구 테스트동 1-1")
//                .facilitiesServices(FacilitiesService.노래방 + "." + FacilitiesService.사우나)
//                .build();
//
//        placeRepository.save(place);
//
//        Room room = Room.builder()
//                .business(place)
//                .name("테스트룸")
//                .standardPrice(80000L)
//                .standardPersonNum(2L)
//                .maximumPersonNum(2L)
//                .noSmoking(true)
//                .information("와인 무료제공 스탠다드 트윈")
//                .checkinStarted(LocalTime.of(15, 0))
//                .build();
//
//        roomRepository.save(room);
//
//        Review review = Review.builder()
//                .score(10L)
//                .consumer(consumer)
//                .content("너무 좋아요.")
//                .room(room)
//                .build();
//
//        reviewRepository.save(review);
//    }

//    @Test
//    public void 점수_입력_오류(){
//        Consumer consumer = (Consumer) userRepository.findAll().get(0);
//        Room room = roomRepository.findAll().get(0);
//
//        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
//            Review review = Review.builder()
//                    .score(11L)
//                    .consumer(consumer)
//                    .content("너무 좋아요.")
//                    .room(room)
//                    .build();
//        });
//
//        assertEquals(exception.getMessage(), "0 ~ 10점 사이만 입력할 수 있습니다.");
//    }

}

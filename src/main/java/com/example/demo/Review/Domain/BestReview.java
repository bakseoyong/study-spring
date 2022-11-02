package com.example.demo.Review.Domain;

//별도의 테이블로 구현하고
//베스트리뷰에서 꺼내오고
//일반 리뷰에서 꺼내오는방식으로

import com.example.demo.Room.Domain.Room;
import com.example.demo.User.Domain.Consumer;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

//최근작성순, 해당 방에 베스트 리뷰가 있을경우 맨 상단에 표시해준다.
//별점 높은순, 별점 낮은순에서는 먼저 조회되고 다른 일반리뷰와 섞여서 정렬을 수행하게 된다.
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC) // 베스트 리뷰 업캐스팅, 다운캐스팅을 위해 PUBLIC
@DiscriminatorValue("BestReview")
public class BestReview extends Review{

    /**
     *     부모 클래스 , 자식 클래스 모두 @Builder 사용시
     *     builder() in BestReview cannot override builder() in Review 오류 메시지가 발생한다.
     *     해당 문제를 해결하기 위해 부모, 자식 클래스에 @SuperBuilder를 통해 해결하려고 하면
     *     해당 상속관계와 연관관계 매핑하는 엔티티의 리포지토리에서 오류가 발생하게 되었다. (cannot ~)
     *     User - Consumer 관계처럼 한 곳에서 빌더 패턴을 없애는 식으로 개선했다.
     *     -> 테스트 코드 정상 동작
     *
     *     발생한 문제
     *     1. writtenAt을 빌더패턴에선 받지 않아서 괜찮았는데 자식 클래스는 빌더패턴을 사용하지 못하므로 writtenAt이 필요한 코드마다 일일이 null을 추가
     */

    public BestReview(Double overall, Double service, Double cleanliness,
                      Double convenience, Double satisfaction, String content,
                      LocalDateTime writtenAt, Consumer consumer, Room room) {
        super(overall, service, cleanliness, convenience, satisfaction, content, writtenAt, consumer, room);
    }

    public static BestReview convertToBestReview(Review review){
        return new BestReview(review.getOverall(), review.getService(), review.getCleanliness(), review.getConvenience(),
                review.getSatisfaction(), review.getContent(), review.getWrittenAt(), review.getConsumer(), review.getRoom());
    }
}

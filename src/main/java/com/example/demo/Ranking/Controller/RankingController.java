package com.example.demo.Ranking.Controller;
;
import com.example.demo.Ranking.Domain.Category;
import com.example.demo.Ranking.Domain.Region;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class RankingController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/top-ranking/logging")
    public void loggingTest(){
        logger.trace("Trace Level 테스트");
        logger.debug("DEBUG Level 테스트");
        logger.info("INFO Level 테스트");
        logger.warn("Warn Level 테스트");
        logger.error("ERROR Level 테스트");
    }

    @GetMapping("/top-ranking/{category-region}")
    public void topRanking(@PathVariable("category-region") String category_region){
        /**
         * category_region를 파싱하는 코드를 짜야 된다. - 하이픈 바 기준으로 앞 뒤로 스플릿을 한다.
         * 앞에는 카테고리, 뒤에는 지역. => 존재하지 않는다면 all-all로 출력해준다. (url이 자동으로 redirect되는건 아니고 그냥 식별하지 못하면 all로 잡는다.)
         */
        String[] strs = category_region.split("-");
        String categoryStr = strs[0];
        String regionStr = strs[1];

        Category category = Category.find(categoryStr);
        Region region = Region.find(regionStr);

        //로깅 시작!
    }
}

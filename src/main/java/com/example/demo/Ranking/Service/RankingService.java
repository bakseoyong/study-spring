package com.example.demo.Ranking.Service;

import com.example.demo.Ranking.Domain.Category;
import com.example.demo.Ranking.Domain.Ranking;
import com.example.demo.Ranking.Domain.Region;
import com.example.demo.Ranking.Domain.ViewPointRanking;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final Logger topRankingLogger = LoggerFactory.getLogger("search");

    //빈도수 체크용 자료구조
    Map<String, Integer> frequency = new HashMap<>();
    //조회를 위한 자료구조
    Map<Map<String, String>, List<String>> search = new HashMap<>();


    public Ranking getRanking(String categoryStr, String regionStr){
        //find 해주는 이유 : ElseGet(전체); + 철자가 인식되지 않는 경우 전부 all로 처리하기 위해서
        Category category = Category.find(categoryStr);
        Region region = Region.find(regionStr);

        String c = category.getDescription();
        String r = region.getDescription();

        List<String> placeIds;
        if(c.equals("all")){
            if(r.equals("all")){
                placeIds = getPlaceIds();
            }
            placeIds = getPlaceIds(region);
        }else if(r.equals("all")){
            placeIds = getPlaceIds(category);
        }else {
            placeIds = getPlaceIds(c, r);
        }

        Map<String, Integer> viewPointPerPlaceId = getViewPointPerPlaceId(placeIds);

        List<Map.Entry<String, Integer>> sortedViewPointPerPlaceId =
                new ArrayList<Map.Entry<String, Integer>>(viewPointPerPlaceId.entrySet());

        Collections.sort(sortedViewPointPerPlaceId, ((o1, o2) -> o2.getValue().compareTo(o1.getValue())));

        //10개만 뽑기.
        List<ViewPointRanking> rank = getRank(sortedViewPointPerPlaceId);

        //랭킹 객체에 맞게 정제 시키기.
        Ranking ranking = new Ranking(c, r, rank);

        return ranking;
    }

    private List<String> getPlaceIds(){
        /**
         * default(all-all)
         */

        List<String> allPlaceIds = new ArrayList<>();

        for(Category c : Category.values()){
            for(Region r : Region.values()){
                String category = c.getDescription();
                String region = r.getDescription();

                Map<String, String> find = new HashMap<>();

                find.put(category, region);

                List<String> placeIds = search.get(find);
                if(placeIds == null)
                    continue; //NullPointerException 방지

                allPlaceIds.addAll(placeIds);
            }
        }

        return allPlaceIds;
    }

    private List<String> getPlaceIds(Category category){
        List<String> includeCategoryPlaceIds = new ArrayList<>();
        String c = category.getDescription();

        for(Region r : Region.values()){
            String description = r.getDescription();

            Map<String, String> find = new HashMap<>();
            find.put(c, description);

            List<String> placeIds = search.get(find);
            if(placeIds == null)
                continue; //NullPointerException 방지

            includeCategoryPlaceIds.addAll(placeIds);
        }

        return includeCategoryPlaceIds;
    }

    private List<String> getPlaceIds(Region region){
        List<String> includeRegionPlaceIds = new ArrayList<>();
        String r = region.getDescription();

        for(Category c : Category.values()){
            String description = c.getDescription();

            Map<String, String> find = new HashMap<>();
            find.put(description, r);

            List<String> placeIds = search.get(find);
            if(placeIds == null)
                continue; //NullPointerException 방지

            includeRegionPlaceIds.addAll(placeIds);
        }

        return includeRegionPlaceIds;
    }

    private List<String> getPlaceIds(String c, String r){
        Map<String, String> find = new HashMap<>();
        find.put(c, r);
        List<String> placeIds = search.get(find);

        return placeIds;
    }

    private Map<String, Integer> getViewPointPerPlaceId(List<String> placeIds){
        Map<String, Integer> viewPointPerPlaceId = new HashMap<>();

        for(String placeId: placeIds){
            Integer viewPoint = frequency.get(placeId);

            viewPointPerPlaceId.put(placeId, viewPoint);
        }

        return viewPointPerPlaceId;
    }

    private List<ViewPointRanking> getRank(List<Map.Entry<String, Integer>> sortedViewPointPerPlaceId){
        List<ViewPointRanking> rank = new ArrayList<>();

        double totalViewPoint = 0;

        for(Map.Entry<String, Integer> entry: sortedViewPointPerPlaceId){
            totalViewPoint += entry.getValue();
        }

        int i = 0;
        for(Map.Entry<String, Integer> entry: sortedViewPointPerPlaceId){
            if(i > 10) break; // 탈출문

            double viewPointPercent = (entry.getValue() / totalViewPoint) * 10 / 10;

            ViewPointRanking viewPointRanking = new ViewPointRanking(entry.getKey(), viewPointPercent);

            rank.add(viewPointRanking);

            i++; //증감식
        }

        return rank;
    }

    //한 시간에 한 번만 호출된다.
    public void createDataStructure() {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new FileReader("/Users/bakseoyong/Downloads/demo/logs/search.log"));
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] logs = line.split(",");
                String time = logs[0];
                String category = logs[1];
                String region = logs[2];
                String placeId = logs[3];

                if (frequency.containsKey(placeId)) {
                    frequency.put(placeId, frequency.get(placeId) + 1);
                } else {
                    frequency.put(placeId, 1);
                }

                Map<String, String> innerMap = new HashMap<>();
                innerMap.put(category, region);

                List<String> placeIds = search.get(innerMap);
                if (placeIds == null) {
                    List<String> newPlaceIds = new ArrayList<>();
                    newPlaceIds.add(placeId);
                    search.put(innerMap, newPlaceIds);
                } else {
                    placeIds.add(placeId);
                    search.put(innerMap, placeIds);
                }
            }

            System.out.println("===== 전역변수_자료구조에_데이터_삽입 =====");
            System.out.println("frequency size is : " + frequency.size());
            System.out.println("search size is : " + search.size());
            System.out.println("===================================");

            //자료구조를 하나 더 만들어서 정렬할 필요가 없다.
//            List<String> listKeySet = new ArrayList<>(frequency.keySet());
//
//            Collections.sort(listKeySet, (value1, value2) -> frequency.get(value2).compareTo(frequency.get(value1)));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

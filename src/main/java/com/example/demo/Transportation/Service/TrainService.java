package com.example.demo.Transportation.Service;

import com.example.demo.Transportation.Domain.TrainStation;
import com.example.demo.Transportation.Domain.Ticket;
import com.example.demo.Transportation.Domain.TrainCityVO;
import com.example.demo.Transportation.Dto.TicketDto;
import com.example.demo.Transportation.Repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainService {

    @Value("${openapi.tago.encoding}")
    private String key;

    @Autowired
    private StationRepository stationRepository;

    public JSONArray getTickets(String dptStationId, String arrStationId, LocalDate date){
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getStrtpntAlocFndTrainInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*데이터 타입(xml, json)*/
            urlBuilder.append("&" + URLEncoder.encode("depPlaceId", "UTF-8") + "=" + URLEncoder.encode(dptStationId, "UTF-8")); /*출발기차역ID [상세기능3. 시/도별 기차역 목록조회]에서 조회 가능*/
            urlBuilder.append("&" + URLEncoder.encode("arrPlaceId", "UTF-8") + "=" + URLEncoder.encode(arrStationId, "UTF-8")); /*도착기차역ID [상세기능3. 시/도별 기차역 목록조회]에서 조회 가능*/
            urlBuilder.append("&" + URLEncoder.encode("depPlandTime", "UTF-8") + "=" + URLEncoder.encode(date.format(DateTimeFormatter.ofPattern("yyyyMMdd")), "UTF-8")); /*출발일(YYYYMMDD)*/
            urlBuilder.append("&" + URLEncoder.encode("trainGradeCode", "UTF-8") + "=" + URLEncoder.encode("00", "UTF-8")); /*차량종류코드*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader br;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            conn.disconnect();
            System.out.println(sb.toString());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            System.out.println("jsonobject size is " + jsonObject.size());
            System.out.println(jsonObject.toString());
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            return item;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getTicketsTest(){
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getStrtpntAlocFndTrainInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*데이터 타입(xml, json)*/
            urlBuilder.append("&" + URLEncoder.encode("depPlaceId", "UTF-8") + "=" + URLEncoder.encode("NAT010000", "UTF-8")); /*출발기차역ID [상세기능3. 시/도별 기차역 목록조회]에서 조회 가능*/
            urlBuilder.append("&" + URLEncoder.encode("arrPlaceId", "UTF-8") + "=" + URLEncoder.encode("NAT011668", "UTF-8")); /*도착기차역ID [상세기능3. 시/도별 기차역 목록조회]에서 조회 가능*/
            urlBuilder.append("&" + URLEncoder.encode("depPlandTime", "UTF-8") + "=" + URLEncoder.encode("20211201", "UTF-8")); /*출발일(YYYYMMDD)*/
            urlBuilder.append("&" + URLEncoder.encode("trainGradeCode", "UTF-8") + "=" + URLEncoder.encode("00", "UTF-8")); /*차량종류코드*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader br;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            conn.disconnect();
            System.out.println(sb.toString());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            System.out.println("jsonobject size is " + jsonObject.size());
            System.out.println(jsonObject.toString());
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");
            System.out.println("===========");
            System.out.println(item.toString());
            System.out.println("items size is : " + items.size());


            for(int i = 0; i < item.size(); i++){
                JSONObject object = (JSONObject) item.get(i);
                Long depTime = (Long) object.get("depplandtime");
                System.out.println(depTime);
            }
            //System.out.println("items size is : " + items.size());

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public JSONArray getCities(){
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getCtyCodeList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*데이터 타입(xml, json)*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            return item;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Transactional
    public JSONArray getStationByCityCode(Long cityCode){
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getCtyAcctoTrainSttnList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*데이터 타입(xml, json)*/
            urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + URLEncoder.encode(cityCode.toString(), "UTF-8")); /*시/도 ID*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            return item;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public List<TrainCityVO> convertJSONArrayToTrainCityVO(JSONArray jsonArray){
        List<TrainCityVO> trainCityVOs = new ArrayList<>();

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            Long cityCode = (Long) jsonObject.get("citycode");
            String cityName = (String) jsonObject.get("cityname");

            trainCityVOs.add(new TrainCityVO(cityCode, cityName));
        }

        return trainCityVOs;
    }

    public List<TrainStation> convertJSONArrayToStation(JSONArray jsonArray){
        List<TrainStation> trainStations = new ArrayList<>();

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String nodeId = (String) jsonObject.get("nodeid");
            String nodeName = (String) jsonObject.get("nodename");

            trainStations.add(new TrainStation(nodeId, nodeName));
        }

        return trainStations;
    }

    public List<Ticket> convertJSONArrayToTicket(JSONArray jsonArray){
        List<Ticket> tickets = new ArrayList<>();

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String dptStationName = (String) jsonObject.get("depplacename");
            LocalDateTime dptDateTime = LocalDateTime.parse( (String) jsonObject.get("depplandtime"), DateTimeFormatter.ofPattern("yyyyMMddHHmmss") );
            String arrStationName = (String) jsonObject.get("arrplacename");
            LocalDateTime arrDateTime = LocalDateTime.parse( (String) jsonObject.get("arrplandtime"), DateTimeFormatter.ofPattern("yyyyMMddHHmmss") );
            String trainGrade = (String) jsonObject.get("traingradename");
            Long trainNo = (Long) jsonObject.get("trainno");
            Long adultCharge = (Long) jsonObject.get("adultcharge");

            tickets.add(Ticket.builder()
                    .dptStationName(dptStationName)
                    .dptDateTime(dptDateTime)
                    .arrStationName(arrStationName)
                    .arrDateTime(arrDateTime)
                    .trainGrade(trainGrade)
                    .trainNo(trainNo)
                    .adultCharge(adultCharge)
                    .build());
        }

        return tickets;
    }

    public void saveStationsTest(){
        JSONArray cityJSONArray = getCities();
        List<TrainCityVO> trainCityVOs = convertJSONArrayToTrainCityVO(cityJSONArray);

        List<JSONArray> stationJSONArrays = trainCityVOs.stream()
                .map(trainCityVO -> getStationByCityCode(trainCityVO.getCityCode()))
                .collect(Collectors.toList());

        List<TrainStation> trainStations = new ArrayList<>();
        for(JSONArray stationJSONArray : stationJSONArrays){
            List<TrainStation> temp = convertJSONArrayToStation(stationJSONArray);
            trainStations.addAll(temp);
        }

        System.out.println("========= saveStationsTest ==========");
        System.out.println("stations size is " + trainStations.size());

    }

    public void saveStations(){
        JSONArray cityJSONArray = getCities();
        List<TrainCityVO> trainCityVOs = convertJSONArrayToTrainCityVO(cityJSONArray);

        List<JSONArray> stationJSONArrays = trainCityVOs.stream()
                .map(trainCityVO -> getStationByCityCode(trainCityVO.getCityCode()))
                .collect(Collectors.toList());

        List<TrainStation> trainStations = new ArrayList<>();
        for(JSONArray stationJSONArray : stationJSONArrays){
            List<TrainStation> temp = convertJSONArrayToStation(stationJSONArray);
            trainStations.addAll(temp);
        }

        stationRepository.saveAll(trainStations);
    }

    @Transactional
    public List<TrainStation> getStations(){
        return stationRepository.findAll();
    }

    @Transactional
    public TicketDto showTicketsByInfo(String dptStationId, String arrStationId, LocalDate date){
        JSONArray jsonArray = getTickets(dptStationId, arrStationId, date);
        List<Ticket> tickets = convertJSONArrayToTicket(jsonArray);

        return new TicketDto(tickets, dptStationId, arrStationId);
    }

}

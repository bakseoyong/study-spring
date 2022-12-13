//package com.example.demo.Transportation.Service;
//
//import com.example.demo.Transportation.Domain.BusTerminal;
//import com.example.demo.Transportation.Domain.TrainCityVO;
//import com.example.demo.Transportation.Domain.TrainStation;
//import com.example.demo.Transportation.Repository.BusStationRepository;
//import lombok.RequiredArgsConstructor;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class BusService {
//    @Value("${openapi.tago.encoding}")
//    private String key;
//
//    @Autowired
//    private BusTerminalRepository busTerminalRepository;
//
//    public void saveStations(){
//        JSONArray cityJSONArray = getCities();
//        List<TrainCityVO> trainCityVOs = convertJSONArrayToTrainCityVO(cityJSONArray);
//
//        List<JSONArray> stationJSONArrays = trainCityVOs.stream()
//                .map(trainCityVO -> getStationByCityCode(trainCityVO.getCityCode()))
//                .collect(Collectors.toList());
//
//        List<TrainStation> trainStations = new ArrayList<>();
//        for(JSONArray stationJSONArray : stationJSONArrays){
//            List<TrainStation> temp = convertJSONArrayToStation(stationJSONArray);
//            trainStations.addAll(temp);
//        }
//
//        stationRepository.saveAll(trainStations);
//    }
//
//    public JSONArray getCities(){
//        try {
//            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/ExpBusInfoService/getCtyCodeList"); /*URL*/
//            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key); /*Service Key*/
//            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*데이터 타입(xml, json)*/
//            URL url = new URL(urlBuilder.toString());
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
//            System.out.println("Response code: " + conn.getResponseCode());
//            BufferedReader rd;
//            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//            rd.close();
//            conn.disconnect();
//            System.out.println(sb.toString());
//
//            JSONParser jsonParser = new JSONParser();
//            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
//            JSONObject response = (JSONObject) jsonObject.get("response");
//            JSONObject body = (JSONObject) response.get("body");
//            JSONObject items = (JSONObject) body.get("items");
//            JSONArray item = (JSONArray) items.get("item");
//
//            return item;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Transactional
//    public List<BusTerminal> getTerminals(){
//        return busTerminalRepository.findAll();
//    }
//}

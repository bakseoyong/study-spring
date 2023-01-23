package com.example.demo.Map.Service;

import com.example.demo.Map.Dto.AddressSearchResultDto;
import com.example.demo.Location.Domain.Location;
import com.example.demo.Location.Dto.LocationDto;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Repository.PlaceRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {

    @Value("${ncp.naverMap.client.id}")
    private String clientId;

    @Value("${ncp.naverMap.client.secret}")
    private String clientSecret;

    private final static String reverseGeocoderApiUrl = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";

    private final static String geocoderApiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";

    @Autowired
    private PlaceRepository placeRepository;

    @Transactional
    public List<AddressSearchResultDto> getRelatedAddresses(String address){
        //address 를 못 받고 있다.
        System.out.println("address is : " + address);

    //public void getRelatedAddresses(String address){
        List<AddressSearchResultDto> addressSearchResultDtos = new ArrayList<>();

        try {
            StringBuilder urlBuilder = new StringBuilder(geocoderApiUrl); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("query", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("page ", "UTF-8") + "=" + 1);
            urlBuilder.append("&" + URLEncoder.encode("count", "UTF-8") + "=" + 30);

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            conn.setRequestProperty("Accept", "application/json");

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

            //json parsing
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            JSONArray addresses = (JSONArray) jsonObject.get("addresses");
            for(int i = 0; i < addresses.size(); i++){
                JSONObject object = (JSONObject) addresses.get(i);
                String roadAddress = (String) object.get("roadAddress");
                String jibunAddress = (String) object.get("jibunAddress");
                String x = (String) object.get("x");
                String y = (String) object.get("y");

                addressSearchResultDtos.add(AddressSearchResultDto.builder()
                        .roadAddress(roadAddress)
                        .jibunAddress(jibunAddress)
                        .x(x)
                        .y(y)
                        .build());
            }

            return addressSearchResultDtos;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Transactional
    public void test(){
        String testCoords = "128.12345,37.98776";

        try {
            StringBuilder urlBuilder = new StringBuilder(reverseGeocoderApiUrl); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("request", "UTF-8") + "=" + URLEncoder.encode("coordsToaddr", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("coords", "UTF-8") + "=" + URLEncoder.encode(testCoords, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("output", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveLocation(Long placeId, LocationDto locationDto){
        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
        Location location = Location.builder()
                .lat(Double.parseDouble(locationDto.getLat()) )
                .lng(Double.parseDouble(locationDto.getLng()) )
                .roadAddress(locationDto.getRoadAddress())
                .jibunAddress(locationDto.getJibunAddress())
                .transportation(locationDto.getTransportation())
                .directions(locationDto.getDirections())
                .build();

        location.setPlace(place);
        place.setLocation(location);

        placeRepository.save(place);
    }
}

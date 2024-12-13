package com.example.galmaegil.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GalmaegilService {

    private final WebClient webClient;

    @Value("${api.secret}")
    private String serviceKey;

    public GalmaegilService() {
        this.webClient = WebClient.create();
    }

    public Mono<String> getTourCenterData(int pageNo, String type) {

        String apiUrl = "https://apis.data.go.kr/6260000/InfoOfficeService/getInfoOffice%s?pageNo=%d&numOfRows=1&resultType=json&serviceKey=";

        return webClient.get()
                        .uri(URI.create(String.format(apiUrl, type, pageNo)+serviceKey))
                        .retrieve()
                        .bodyToMono(String.class);
    }

    public Mono<String> getCourseList() {

        String apiUrl = "https://apis.data.go.kr/6260000/fbusangmgcourseinfo/getgmgcourseinfo?&numOfRows=50&pageNo=1&resultType=json&serviceKey=";

        return webClient.get()
                        .uri(URI.create(apiUrl+serviceKey))
                        .retrieve()
                        .bodyToMono(String.class);
    }

    public Mono<String> getAttractionListByPage(int pageSize, int currentPage) {

        String apiUrl = "https://apis.data.go.kr/6260000/fbusangmgtourinfo/getgmgtourinfo?&numOfRows=%d&pageNo=%d&resultType=json&serviceKey=";

        return webClient.get()
                .uri(URI.create(String.format(apiUrl, pageSize, currentPage)+serviceKey))
                .retrieve()
                .bodyToMono(String.class);
    }

    public ResponseEntity<JsonNode> getAttractionDetail(int ucSeq, int rPage, int aPage, int pageSize) {

        String apiUrl1 = "https://apis.data.go.kr/6260000/fbusangmgtourinfo/getgmgtourinfo?&numOfRows=100&pageNo=1&resultType=json&serviceKey=";
        String apiUrl2 = "https://apis.data.go.kr/6260000/fbusangmgtourinfo/getgmgfoodinfo?&numOfRows=100&pageNo=1&resultType=json&serviceKey=";
        String apiUrl3 = "https://apis.data.go.kr/6260000/fbusangmgtourinfo/getgmghotelinfo?&numOfRows=100&pageNo=1&resultType=json&serviceKey=";

        // OPEN API에서 특정 uc_seq에 해당하는 명소 데이터를 반환하는 방법이 존재하지 않아
        // 전체 데이터 조회 후 반복문을 통해 특정 uc_seq에 해당하는 명소 데이터만 추출
        JsonNode sightData = webClient.get()
                                            .uri(URI.create(String.format(apiUrl1)+serviceKey))
                                            .retrieve()
                                            .bodyToMono(JsonNode.class)
                                            .map(data -> {
                                                    JsonNode items = data.path("response")
                                                                             .path("body")
                                                                             .path("items")
                                                                             .path("item");
                                                    if (items.isArray()) {
                                                        for (JsonNode item : items) {
                                                            if (item.path("uc_seq").asInt() == ucSeq) {
                                                                return item;
                                                            }
                                                        }
                                                    }
                                                    return null;
                                            })
                                            .block();
        Double sightLat;
        Double sightLng;

        if (sightData != null) {
            sightLat = sightData.path("lat").asDouble();
            sightLng = sightData.path("lng").asDouble();
        } else {
            throw new IllegalStateException("Sight data is missing");
        }



        // 반복문 돌려서 계산식의 결과 값이 5이하인 데이터만 반환하도록 수정
        ArrayNode restaurantData = webClient.get()
                                               .uri(URI.create(String.format(apiUrl2)+serviceKey))
                                               .retrieve()
                                               .bodyToMono(JsonNode.class)
                                               .map(data -> {
                                                    JsonNode items = data.path("response")
                                                            .path("body")
                                                            .path("items")
                                                            .path("item");

                                                    ObjectMapper mapper = new ObjectMapper();
                                                    ArrayNode filteredRestaurants = mapper.createArrayNode();

                                                    if (items.isArray()) {
                                                        for (JsonNode item : items) {
                                                            Double restlat = item.path("lat").asDouble();
                                                            Double restlng = item.path("lng").asDouble();

                                                            Double latDistance = Math.abs(sightLat - restlat) * 111;
                                                            Double lngDistance = Math.abs(sightLng - restlng) * 90.9;
                                                            double distance = Math.sqrt(latDistance * latDistance + lngDistance * lngDistance);

                                                            if(distance <= 5) {
                                                                ((ObjectNode) item).put("distance", Math.round(distance * 100) / 100.0);
                                                                filteredRestaurants.add(item);
                                                            }
                                                        }
                                                    }
                                                    return filteredRestaurants;
                                               })
                                               .block();

        // 반복문 돌려서 계산식의 결과 값이 5이하인 데이터만 반환하도록 수정
        ArrayNode accommodationData = webClient.get()
                                                 .uri(URI.create(String.format(apiUrl3)+serviceKey))
                                                 .retrieve()
                                                 .bodyToMono(JsonNode.class)
                                                 .map(data -> {
                                                      JsonNode items = data.path("response")
                                                            .path("body")
                                                            .path("items")
                                                            .path("item");

                                                     ObjectMapper mapper = new ObjectMapper();
                                                     ArrayNode filteredAccommodations = mapper.createArrayNode();

                                                      if (items.isArray()) {
                                                        for (JsonNode item : items) {
                                                            Double accomLat = item.path("lat").asDouble();
                                                            Double accomLng = item.path("lng").asDouble();

                                                            Double latDistance = Math.abs(sightLat - accomLat) * 111;
                                                            Double lngDistance = Math.abs(sightLng - accomLng) * 90.9;
                                                            double distance = Math.sqrt(latDistance * latDistance + lngDistance * lngDistance);

                                                            if(distance <= 5) {
                                                                ((ObjectNode) item).put("distance", Math.round(distance * 100) / 100.0);
                                                                filteredAccommodations.add(item);
                                                            }
                                                        }
                                                      }
                                                      return filteredAccommodations;
                                                 })
                                                 .block();

        ObjectNode sightDataNode = (ObjectNode) sightData; // sightData를 ObjectNode로 변환

        int rStart = (rPage - 1) * pageSize;
        int rEnd = Math.min(rStart + pageSize, restaurantData.size());
        ObjectMapper mapper1 = new ObjectMapper();
        ArrayNode paginatedRestaurants = mapper1.createArrayNode();

        for (int i = rStart; i < rEnd; i++) {
            paginatedRestaurants.add(restaurantData.get(i));
        }

        // restaurants 키에 restaurantData 추가
        sightDataNode.set("restaurants", paginatedRestaurants);
        sightDataNode.put("totalRestaurants", restaurantData.size());

        int aStart = (aPage - 1) * pageSize;
        int aEnd = Math.min(aStart + pageSize, accommodationData.size());
        ObjectMapper mapper2 = new ObjectMapper();
        ArrayNode paginatedAccommodations = mapper2.createArrayNode();

        for (int j = aStart; j < aEnd; j++) {
            paginatedAccommodations.add(accommodationData.get(j));
        }

        sightDataNode.set("accommodations", paginatedAccommodations);
        sightDataNode.put("totalAccommodations", accommodationData.size());

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(sightDataNode);

    }
}

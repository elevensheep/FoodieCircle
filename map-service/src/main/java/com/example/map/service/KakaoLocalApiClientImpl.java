package com.example.map.service;

import com.example.map.dto.KakaoPlaceDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class KakaoLocalApiClientImpl implements KakaoLocalApiClient {

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    private final RestTemplate restTemplate;
    private final String apiKey;

    public KakaoLocalApiClientImpl(
            @Value("${kakao.api.key:}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
    }

    @Override
    public List<KakaoPlaceDto> searchByKeyword(String keyword, Double x, Double y, Integer radius) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        String url = UriComponentsBuilder.fromHttpUrl(KAKAO_API_URL)
                .queryParam("query", keyword)
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("radius", radius)
                .queryParam("category_group_code", "FD6")
                .toUriString();

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<>() {});

            if (response.getBody() == null || !response.getBody().containsKey("documents")) {
                return Collections.emptyList();
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");

            return documents.stream()
                    .map(doc -> new KakaoPlaceDto(
                            (String) doc.get("id"),
                            (String) doc.get("place_name"),
                            (String) doc.get("address_name"),
                            (String) doc.get("category_name"),
                            (String) doc.get("x"),
                            (String) doc.get("y")
                    ))
                    .toList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}

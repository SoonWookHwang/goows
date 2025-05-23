package com.lgcns.goows.components.naver;

import com.lgcns.goows.dto.NewsSearchDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NaverApiComponent {
    @Value("${naver.api.client-id}")
    String clientId;

    @Value("${naver.api.client-secret}")
    String clientSecret;

    @Value("${naver.api.news-url}")
    String newsSearchUrl;

    public String getNewsDataFromNaver(NewsSearchDto dto){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        String sort = "sim";
        String url = String.format("%s?query=%s&start=%d&display=%d&sort=%s", newsSearchUrl, dto.getKeyword(), dto.getOffset(), dto.getLimit(), sort);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        return responseEntity.getBody();
    }
}

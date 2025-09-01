package com.boot.loiteBackend.web.payment.client;

import com.boot.loiteBackend.web.payment.config.PortOneConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PortOneClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final PortOneConfig portOneConfig;

    public String getAccessToken() {
        String url = portOneConfig.getUrl() + "/login/api-secret";

        Map<String, String> body = new HashMap<>();
        body.put("apiSecret", portOneConfig.getSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, new HttpEntity<>(body, headers), Map.class);

        Map<String, Object> res = response.getBody();
        System.out.println(">>> PortOne 토큰 응답 = " + res);

        if (res == null || !res.containsKey("accessToken")) {
            throw new RuntimeException("토큰 발급 실패: " + res);
        }

        return (String) res.get("accessToken");
    }

    public Map<String, Object> getPaymentByTxId(String txId) {
        String token = getAccessToken();
        String url = portOneConfig.getUrl() + "/payments/" + txId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + token);

        ResponseEntity<Map> response =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

        Map<String, Object> body = response.getBody();
        System.out.println(">>> PortOne 결제 단건조회 응답 = " + body);
        return body;
    }

}

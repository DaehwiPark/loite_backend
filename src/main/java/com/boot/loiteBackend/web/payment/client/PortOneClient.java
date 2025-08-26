package com.boot.loiteBackend.web.payment.client;

import com.boot.loiteBackend.web.payment.config.PortOneConfig;
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

    /** 액세스 토큰 발급 */
    public String getAccessToken() {
        String url = portOneConfig.getUrl() + "/users/getToken";

        Map<String, String> body = new HashMap<>();
        body.put("imp_key", portOneConfig.getKey());
        body.put("imp_secret", portOneConfig.getSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> responseData = (Map<String, Object>) responseBody.get("response");

        return (String) responseData.get("access_token");
    }

    /** 결제 단건 조회 */
    public Map<String, Object> getPaymentByImpUid(String impUid) {
        String token = getAccessToken();
        String url = portOneConfig.getUrl() + "/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }
}

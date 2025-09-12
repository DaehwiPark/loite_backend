package com.boot.loiteBackend.web.payment.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SignatureUtil {

    @Value("${portone.webhook.secret}")
    private String portoneWebhookSecret;

    public boolean verifySignature(String rawBody, String signature) {
        try {
            String expected = hmacSha256(rawBody, portoneWebhookSecret);
            return expected.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    private String hmacSha256(String data, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}


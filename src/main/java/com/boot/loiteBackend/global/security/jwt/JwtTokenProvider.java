package com.boot.loiteBackend.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public String createToken(Long userId, String email, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId)); // "sub": userId
        claims.put("username", email);                                    // "username": email
        claims.put("role", role);                                         // "role": role
        claims.put("tokenType", "access");                                // "tokenType": access

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getAccessTokenValidity());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)          // "iat"
                 .setExpiration(expiry)     // "exp"
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    public String createRefreshToken() {
        Claims claims = Jwts.claims();
        claims.put("tokenType", "refresh"); // "tokenType": refresh

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getRefreshTokenValidity());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)          // "iat"
                .setExpiration(expiry)     // "exp"
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        return Long.parseLong(Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public String getUserRole(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    public String getTokenType(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .get("tokenType", String.class);
    }

    public long getAccessTokenValidity() {
        return jwtProperties.getAccessTokenValidity();
    }

    public long getRefreshTokenValidity() {
        return jwtProperties.getRefreshTokenValidity();
    }
}

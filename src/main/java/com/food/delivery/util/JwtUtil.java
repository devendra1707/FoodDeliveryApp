package com.food.delivery.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private  String SECRET_KEY;

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromToken(String token) {

        log.info("This is The getUserNameFromToken method");
        return getClaimsFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {

        log.info("This is The getClaimsFromToken method");
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {

        log.info("This is The getAllClaimsFromToken method");

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validToken(String token, UserDetails userDetails) {
        log.info("This is The validToken method");
        String userName = getUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isExpiredDate(token));
    }

    private boolean isExpiredDate(String token) {

        log.info("This is The isExpiredDate method");
        final Date tokenExpiredDate = expiredDateFromToken(token);
        return tokenExpiredDate.before(new Date());
    }

    private Date expiredDateFromToken(String token) {

        log.info("This is The expiredDateFromToken method");
        return getClaimsFromToken(token, Claims::getExpiration);
    }
}

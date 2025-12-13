package com.qmeetx.authservice.infrastructure.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Component
@Slf4j
public class JwtTokenProvider {

    private final Key secretKey;
    private final long expirationTime;

    public JwtTokenProvider(
            @Value("${security.jwt.secretkey}") String secret,
            @Value("${security.jwt.expiration}") long expirationTime
    ) {
        //  Support both raw and Base64 secrets
        byte[] keyBytes;
        if (isBase64Encoded(secret)) {
            keyBytes = Base64.getDecoder().decode(secret);
        } else {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationTime = expirationTime;
    }

    //  Generate JWT
    public String generateToken(String id,String email, String name, boolean isVerified, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(email)
                .addClaims(Map.of(
                        "sub",id,
                        "name", name,
                        "email", email,
                        "isVerified", isVerified,
                        "role", role

                ))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //  Validate JWT with better error handling
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Token is expired {} ",e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported Token {} ",e.getMessage());
        } catch (MalformedJwtException e) {
            log.info("JWT malformed: {}", e.getMessage());
        } catch (SignatureException e) {
            log. info("Invalid JWT signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    //  Extract subject (email)
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();


    }

    //  Extract role
    public String getRoleFromToken(String token) {
        return (String) parseClaims(token).get("role");
    }

    //  Extract verification status
    public boolean isVerifiedFromToken(String token) {
        Object val = parseClaims(token).get("isVerified");
        return val instanceof Boolean && (Boolean) val;
    }

    //  Extract name
    public String getNameFromToken(String token) {
        return (String) parseClaims(token).get("name");
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody();
    }

    // Detect Base64 secret
    private boolean isBase64Encoded(String value) {
        try {
            Base64.getDecoder().decode(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

package com.example.shopping_cart.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;


// JWT हेल्पर क्लास जो टोकन बनाता है और वैधता जांचता है
@Slf4j

@Component
public class JwtHelper {

    // गुप्त कुंजी जिसे टोकन पर हस्ताक्षर करने के लिए उपयोग किया जाता है
//    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String SECRET_KEY_STRING = "wM5xL2fWkDPJkqDhd9P9rVtYgyy5TYHuzh5pCu6tH+s=";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY_STRING));


    // टोकन की वैधता अवधि (मिनट में)
    private static final int DAYS = 86400;

    // उपयोगकर्ता के ईमेल के लिए एक JWT टोकन बनाएँ
    public static String generateToken(String adminName) {
        var now = Instant.now();
String jwt = Jwts.builder()
                .subject(adminName) // टोकन का विषय
                .issuedAt(Date.from(now)) // जारी करने की तिथि
                .expiration(Date.from(now.plus(DAYS, ChronoUnit.DAYS))) // समाप्ति तिथि
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // हस्ताक्षर करें
                .compact(); // टोकन को स्ट्रिंग में बदलें
        System.out.println("Received token: " + jwt);

        return jwt;
    }

    // टोकन से उपयोगकर्ता नाम निकालें
    public static String extractUsername(String token) {

        return getTokenBody(token).getSubject();
    }

    // टोकन की वैधता की जांच करें
    public static Boolean validateToken(String token, UserDetails userDetails) {

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // यह method टोकन से claim (payload) निकालता है
    private static Claims getTokenBody(String token) {
        try {
            // Null या खाली टोकन की जांच
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("टोकन रिक्त है या null है");
            }

            // सही स्ट्रक्चर: 2 dots यानी 3 parts (header.payload.signature)
            if (token.chars().filter(ch -> ch == '.').count() != 2) {
                throw new IllegalArgumentException("टोकन का फॉर्मेट अमान्य है (डॉट्स गिनती गलत)");
            }

            return Jwts
                    .parser()                          // JWT पार्सर बनाने की प्रक्रिया शुरू करें
                    .setSigningKey(SECRET_KEY)         // टोकन को verify करने के लिए गुप्त कुंजी सेट करें
                    .build()                           // पार्सर को final रूप दें
                    .parseSignedClaims(token)          // दिए गए टोकन को पार्स करें और claims निकालें
                    .getPayload();                     // केवल payload (claims) हिस्सा वापस करें
        } catch (ExpiredJwtException e) {
        throw new RuntimeException("टोकन समाप्त हो गया है: " + e.getMessage());
    } catch (SignatureException e) {
        throw new RuntimeException("सिग्नेचर अमान्य है: " + e.getMessage());
    } catch (
    MalformedJwtException e) {
        throw new RuntimeException("JWT फॉर्मेट अमान्य है: " + e.getMessage());
    } catch (IllegalArgumentException | UnsupportedJwtException e) {
        throw new RuntimeException("अवैध JWT टोकन: " + e.getMessage());
    }

    }

    // जांचें कि टोकन समाप्त हुआ या नहीं
    private static boolean isTokenExpired(String token) {
        Claims claims = getTokenBody(token); // टोकन से claims प्राप्त करें

        return claims.getExpiration().before(new Date()); // expiration तारीख अभी की तारीख से पहले है या नहीं
    }
}
package com.botcamp.common.utils;

import com.botcamp.common.jwt.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {
    private static final String BEARER = "Bearer";
    private static final String SPACE = " ";
    // Retrieve username from jwt token
    public static String getUsernameFromToken(String token, String secret) {
        return getClaimFromToken(token, Claims::getSubject, secret);
    }

    // Retrieve expiration date from jwt token
    public static Date getExpirationDateFromToken(String token, String secret) {
        return getClaimFromToken(token, Claims::getExpiration, secret);
    }

    public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver, String secret) {
        if (token.startsWith(BEARER)) {
            token = token.split(SPACE)[1];
        }
        final Claims claims = getAllClaimsFromToken(token, secret);
        return claimsResolver.apply(claims);
    }

    // To retrieve any information from token we will need the secret key
    private static Claims getAllClaimsFromToken(String token, String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // Check if the token has expired
    private static Boolean isTokenExpired(String token, String secret) {
        final Date expiration = getExpirationDateFromToken(token, secret);
        return expiration.before(new Date());
    }

    // Generate token for user
    public static JwtToken generateToken(UserDetails userDetails, String secret, Long tokenValidity, ChronoUnit unit) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), secret, tokenValidity, unit);
    }

    // While creating the token -
    // 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    // 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //    compaction of the JWT to a URL-safe string
    private static JwtToken doGenerateToken(Map<String, Object> claims, String subject, String secret, Long tokenValidity, ChronoUnit unit) {
        LocalDateTime expirationDateTime = LocalDateTime.now().plus(tokenValidity, unit);
        Date expirationDate = DateUtils.localDateTimeToDate(expirationDateTime);
        String token = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return new JwtToken(token, expirationDateTime);
    }

    // Validate token
    public static Boolean validateToken(String token, UserDetails userDetails, String secret) {
        final String username = getUsernameFromToken(token, secret);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, secret));
    }

}

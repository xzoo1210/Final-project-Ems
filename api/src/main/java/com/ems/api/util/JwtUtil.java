package com.ems.api.util;

import com.ems.api.dto.response.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    private static final String SECRET_KEY = "secret";
    public static final String CLAIM_REINVITE = "reinvite";
    public static final String CLAIM_CONTACTID = "user";
    public static final String CLAIM_EVENTID = "event";
    // By default, token is expired in 24 hours
    private final int jwtTokenExpireInSeconds = 86400;

    // By default, token for remember me is expired in 30 days
    private final int jwtTokenExpireForRememberMe = 30;

    // By default, verify token is expired in 24 hours
    private final int verifyTokenExpireInSeconds = 86400;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public LoginResponse generateToken(UserDetails userDetails, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<>();
        Date expiryDate;
        Calendar now = Calendar.getInstance();
        if (rememberMe) {
            now.add(Calendar.DATE, this.jwtTokenExpireForRememberMe);
            expiryDate = now.getTime();
        } else {
            now.add(Calendar.SECOND, this.jwtTokenExpireInSeconds);
            expiryDate = now.getTime();
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwtToken(createToken(claims, userDetails.getUsername(), expiryDate));
        loginResponse.setTokenExpiredAt(expiryDate);
        return loginResponse;
    }

    public String createToken(Map<String, Object> claims, String subject, Date expiryDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
    public static String createToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractEmail(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//    public static void main(String[] args) {
//        Calendar now = Calendar.getInstance();
//        now.add(Calendar.DATE,-1);
//
//        System.out.println(Jwts.builder()
//                .setSubject("dngbi97@gmail.com")
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(now.getTime())
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact());
//    }
}

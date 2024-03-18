package org.fexisaf.flexisafadvencefour.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "pZcMPRVpD6yGlEJFY6m6r8/vFzmgLF"+
            "JvORyxQbnK7yNLDzRBYh0JIKk0eARjWrqewVZk+Iax3k9lu0VRXhT1nSnbl3AX"+
            "xLiHCXalWkAHNWtVNXjH976A4ykJ3zgJGaTbUWwrt1cIlIxm3gHW6WRb/0m2WM"+
            "ZdgdCn0XOrNVkSpSt0Flwe03c2z7KPZzUyOYcCadJP37I4Du1HQdsBiqbp2"+
            "Q8Qr0xqrw3DudVD1tPERLKuokjCgdcf8AihnTzSgnjVobl+WLTFjC6KYz0d"+
            "3Ux8tQZbrvyMR49Y1QSQMCFJDBiiN7NO8DMWwxUyJr+y+mJRBVNXT2FHsxl"+
            "jZLmYQQh4HFa28AF64URcYekNcurefK0=";

    public String extractUserName(String token){

        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(UserDetails userDetails, String token){
        String user = extractUserName(token);
        return user.equals(userDetails.getUsername()) && !isTokenExpired(token) ;
    }

    private boolean isTokenExpired(String token ){
        return extractDate(token).before(new Date());
    }

    private Date extractDate(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken(Map<String, Object> extraClaim,
                                 UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaim)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        Claims claims = extractAllClaim(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaim(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private Key getSigningKey(){
        byte[] bit = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bit);
    }
}

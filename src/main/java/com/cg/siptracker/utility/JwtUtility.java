package com.cg.siptracker.utility;
import com.cg.siptracker.model.User;
import com.cg.siptracker.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtility {

    @Autowired
    private UserRepository userRepository;

    private static final String SECRET_KEY = "599abcd6769xyz1234567890987654321";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());


    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 20*60*1000))
//                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token){
        try{
            System.out.println(token);
            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("Getting email => " + claims);
            return claims.getSubject();
        }catch(Exception e){
            // Token expired
            return e.getMessage();
        }
    }

    public boolean validateToken(String token, String userEmail){
        final String email = extractEmail(token);
        boolean isTokenPresent = true;
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null && token == null){
            isTokenPresent = false;
        }
        final boolean valid = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());

        return (email.equals(userEmail) && !valid && isTokenPresent);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key) // Use your actual secret key
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}

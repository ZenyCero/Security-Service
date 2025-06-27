package com.social.securityservice.domain.jwt;

import com.social.securityservice.dto.TokenDTO;
import com.social.securityservice.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.expiration}")
    private int EXPIRATION;

    public TokenDTO createToken(UserDetails userDetails){
        return new TokenDTO(generateToken(userDetails));
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(EXPIRATION, ChronoUnit.MINUTES)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @SneakyThrows
    public void validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);

        } catch (ExpiredJwtException ex){
            throw new CustomException("Token expired", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new CustomException("Bad request token", HttpStatus.BAD_REQUEST);
        }
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    public List<SimpleGrantedAuthority> getRoles(Claims claims){
        Object objectRoles = claims.get("roles");
        if (objectRoles instanceof List<?>){
            List<Map<String, String>> roles = (List<Map<String, String>>) objectRoles;

            return roles.stream()
                    .map(rol -> rol.get("authority"))
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }
        return List.of();
    }

    private Key getSignKey(){
        byte[] secret = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(secret);
    }
}

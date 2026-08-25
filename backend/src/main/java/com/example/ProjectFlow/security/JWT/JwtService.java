
// packages
package com.example.ProjectFlow.security.JWT;

// imports
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.Date;

// jwt imports
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

// import configs
import com.example.ProjectFlow.config.JwtProperties;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserDTO;


@Service
public class JwtService {
 
   // properties
   private final JwtProperties jwtProperties;


   // constructor - dependency injection
   public JwtService(JwtProperties jwtProperties) {
      this.jwtProperties = jwtProperties;
   }


   // generate token
   public String generateToken(UserDTO user) {
      Map<String, Object> claims = new HashMap<>();
      claims.put("id", user.id());
      claims.put("name", user.name());
      claims.put("email", user.email());

      return Jwts.builder()
         .setClaims(claims)
         .setSubject(user.email())
         .setIssuedAt(new Date()) // creation timestamp
         .setExpiration(new Date(System.currentTimeMillis() + this.jwtProperties.getExpiration()))
         .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
         .compact();
   }

   // extract email
   public String extractEmail(String token) {
      final Claims claims = extractAllClaims(token);
      return claims.getSubject();
   }
   
   // extract id
   public UUID extractId(String token) {
      final Claims claims = extractAllClaims(token);
      return claims.get("id", UUID.class);      
   }
   
   // extract expiration
   public Date extractExpiration(String token) {
      final Claims claims = extractAllClaims(token);
      return claims.getExpiration();      
   }

   // token validation check
   public Boolean validateToken(String token, UserDTO user) {
      final String email = extractEmail(token);
      boolean validation = email.equals(user.email()) && !isTokenExpired(token);
      
      return validation;
   }


   //// private methods


   // get signing key
   private Key getSigningKey() {
      byte[] keyBytes = this.jwtProperties.getSecret().getBytes();
      return Keys.hmacShaKeyFor(keyBytes); // creates a HMAC-SHA256 key from bytes of my secret key
   }

   // extract all claims
   private Claims extractAllClaims(String token) {
      return Jwts.parserBuilder()
         .setSigningKey(this.getSigningKey())
         .build()
         .parseClaimsJws(token) // parse + valid
         .getBody();
   }

   // token expired check
   private Boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
   }

}
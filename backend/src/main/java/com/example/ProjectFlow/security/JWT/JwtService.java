
// packages
package com.example.ProjectFlow.security.JWT;

// imports
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.function.Function;

// jwt imports
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserDTO;

// import configs
import com.example.ProjectFlow.config.JwtProperties;


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

   // extract a claim
   public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims); // get a specific claim
   }

   // get email
   public String extractEmail(String token) {
      return extractClaim(token, Claims::getSubject);
   }
   
   // get id
   public Long extractId(String token) {
      return extractClaim(token, claims -> claims.get("id", Long.class));
   }
   
   // get expiration
   public Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
   }

   // validate token
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
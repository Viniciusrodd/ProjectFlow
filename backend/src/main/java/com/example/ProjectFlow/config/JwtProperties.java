
// packages
package com.example.ProjectFlow.config;

// imports
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
 
   // properties
   private String secret;   
   private Long expiration;
   
   // getters
   public String getSecret() { return secret; }
   public Long getExpiration() { return expiration; }

   // setters
   public void setSecret(String secret) { this.secret = secret; }
   public void setExpiration(Long expiration) { this.expiration = expiration; }

}
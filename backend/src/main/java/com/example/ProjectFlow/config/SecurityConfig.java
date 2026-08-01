
// packages
package com.example.ProjectFlow.config;

// imports
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

// import security
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

   // properties
   private final JwtFilter jwtFilter;

   // constructor - dependency injection
   public SecurityConfig(JwtFilter jwtFilter) {
      this.jwtFilter = jwtFilter;
   }

 
   // security filter chain
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
         .csrf(csfr -> csfr.disable()) // csft disable
         .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/auth/**").permitAll() // auth endpoints free
            .anyRequest().authenticated() // any other endpoint require authentication
         )
         .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // stateless app -> its jwt based
         )
         .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
      
      return http.build();
   }

}
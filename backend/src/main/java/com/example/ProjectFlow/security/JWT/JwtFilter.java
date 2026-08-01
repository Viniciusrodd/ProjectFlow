
// packages
package com.example.ProjectFlow.security.JWT;

// imports
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// import services
import com.example.ProjectFlow.modules.user.service.UserService;

// jakarta imports
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

// security imports
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserDTO;


@Component 
public class JwtFilter extends OncePerRequestFilter {
 
   // properties
   private final JwtService jwtService;
   private final UserService userService;

   // constructor - dependency injection
   public JwtFilter(JwtService jwtService, UserService userService) {
      this.jwtService = jwtService;
      this.userService = userService;
   }


   // do filter internal - override method
   @Override
   protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
   ) throws ServletException, IOException {
      final String authHeader = request.getHeader("Authorization");
      final String jwt;
      final String userEmail;

      // filter chain validation
      if(authHeader == null || !authHeader.startsWith("Bearer ")) {
         filterChain.doFilter(request, response);
         return;
      }

      // get properties values
      jwt = authHeader.substring(7);
      userEmail = this.jwtService.extractEmail(jwt);

      // token email / user authentication in actual request - validation
      if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
         UserDTO user = this.userService.findByEmail(userEmail);

         // token validation
         if(this.jwtService.validateToken(jwt, user)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
               user, // this obj. creation represents the authenticated user in Spring Security
               null,
               null
            );
            
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // add metadatas(IP, section, etc.) - util for logs/audit
            SecurityContextHolder.getContext().setAuthentication(authToken); // set user authentication for the rest of request
         }
      }

      // advance filter flow
      filterChain.doFilter(request, response);
   }

}
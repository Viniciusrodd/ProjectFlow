
// packages
package com.example.ProjectFlow.modules.auth.service;

// imports
import org.springframework.stereotype.Service;

// security imports
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class PasswordService {
 
   // properties
   private final PasswordEncoder passwordEncoder;

   // constructor
   public PasswordService() {
      this.passwordEncoder = new BCryptPasswordEncoder();
   }

   // encrypting
   public String encryptPassword(String password) {
      return this.passwordEncoder.encode(password);
   }

   // verifying passwords match
   public boolean matches(String password, String encodedPassword) {
      return this.passwordEncoder.matches(password, encodedPassword);
   }

}
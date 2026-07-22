
// packages
package com.example.ProjectFlow.exception;


public abstract class BaseException extends RuntimeException {
 
   // properties
   protected final int statusCode;

   // constructor
   protected BaseException(int statusCode, String message) {
      super(message);
      this.statusCode = statusCode;
   }

   // getters
   public int getStatusCode() { return statusCode; }

}
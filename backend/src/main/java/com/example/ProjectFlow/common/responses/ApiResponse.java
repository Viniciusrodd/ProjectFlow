
// packages
package com.example.ProjectFlow.common.responses;


public class ApiResponse<T> {
   
   // properties
   private final String message;
   private final int statusCode;
   private final T data;
   
   // response
   public ApiResponse(String message, int statusCode, T data) {
      this.message = message;
      this.statusCode = statusCode;
      this.data = data;
   }

   // getters
   public String getMessage() { return message; }
   public int getStatusCode() { return statusCode; }
   public T getData() { return data; }

}
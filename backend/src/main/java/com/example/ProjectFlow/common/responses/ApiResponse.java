
// packages
package com.example.ProjectFlow.common.responses;

// imports
import java.util.Optional;


public class ApiResponse<T> {
   
   // properties
   private final String message;
   private final int statusCode;
   private final boolean success;
   private final Optional<T> data;
   
   // response
   private ApiResponse(Builder<T> builder) {
      this.message = builder.message;
      this.statusCode = builder.statusCode;
      this.success = builder.success;
      this.data = Optional.ofNullable(builder.data);
   }

   // getters
   public String getMessage() { return message; }
   public int getStatusCode() { return statusCode; }
   public boolean getSuccess() { return success; }
   public Optional<T> getData() { return data; }


   /////


   // builder
   public static class Builder<T> {
      private String message;
      private int statusCode;
      private boolean success;
      private T data;

      public Builder<T> message(String message) {
         this.message = message;
         return this;
      }

      public Builder<T> statusCode(int statusCode) {
         this.statusCode = statusCode;
         return this;
      }

      public Builder<T> success(boolean success) {
         this.success = success;
         return this;
      }

      public Builder<T> data(T data) {
         this.data = data;
         return this;
      }

      // final build
      public ApiResponse<T> build() {
         return new ApiResponse<>(this);
      }
   }

}
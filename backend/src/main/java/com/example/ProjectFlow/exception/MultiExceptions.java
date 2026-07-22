
// packages
package com.example.ProjectFlow.exception;


public class MultiExceptions extends BaseException {
   
   // constructor
   public MultiExceptions(int statusCode, String message) {
      super(statusCode, message);
   }

   //////

   // 404 - not found
   public static MultiExceptions notFound(String message) {
      return new MultiExceptions(404, message);
   }

   // 400 - bad request
   public static MultiExceptions badRequest(String message) {
      return new MultiExceptions(400, message);
   }

   // 401 - unauthorized
   public static MultiExceptions unauthorized(String message) {
      return new MultiExceptions(401, message);
   }

   // 403 - forbidden
   public static MultiExceptions forbidden(String message) {
      return new MultiExceptions(403, message);
   }

   // 500 - internal
   public static MultiExceptions internal(String message) {
      return new MultiExceptions(500, message);
   }

   // 409 - duplicate
   public static MultiExceptions duplicate(String message) {
      return new MultiExceptions(409, message);
   }

   // 422 - invalid data
   public static MultiExceptions invalid(String message) {
      return new MultiExceptions(422, message);
   }

}
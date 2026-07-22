
// packages
package com.example.ProjectFlow.exception;

// http imports
import org.springframework.http.ResponseEntity;

// spring web imports
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// import apiResponse
import com.example.ProjectFlow.common.responses.ApiResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {
 
   // handle method
   @ExceptionHandler(BaseException.class)
   public ResponseEntity<ApiResponse<Void>> handle(BaseException error) {
      ApiResponse<Void> response = new ApiResponse.Builder<Void>()
         .success(false)
         .statusCode(error.getStatusCode())
         .message(error.getMessage())
         .build();

      return ResponseEntity.status(error.getStatusCode()).body(response);
   }

}
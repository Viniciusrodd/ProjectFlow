
// packages
package com.example.ProjectFlow.exception.multiExceptions;

// import base exception
import com.example.ProjectFlow.exception.BaseException;


public class BusinessException extends BaseException {
   
   // constructor
   public BusinessException(String message) {
      super(400, message);
   }

}

// packages
package com.example.ProjectFlow.common.interfaces.crudBase;

// import local date time
import java.time.LocalDateTime;


public interface softDelete {
 
   boolean isDeleted();
   LocalDateTime getDeletedAt();

}
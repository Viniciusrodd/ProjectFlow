
// packages
package com.example.ProjectFlow.common.interfaces.permissions;

// import local date time
import java.time.LocalDateTime;


public interface Auditable {
   LocalDateTime getCreatedAt();
   LocalDateTime getUpdatedAt();
}

// packages
package com.example.ProjectFlow.common.interfaces.permissions;

// import uuid
import java.util.UUID;


public interface Admin {
   
   UUID getAdminId();
   void setAdminId(UUID adminId);
   default boolean isAdmin(UUID userId) {
      return getAdminId() != null && getAdminId().equals(userId);
   }

}
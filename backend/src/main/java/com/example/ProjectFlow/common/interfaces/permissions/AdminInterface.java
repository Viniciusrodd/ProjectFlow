
// packages
package com.example.ProjectFlow.common.interfaces.permissions;

// import uuid
import java.util.UUID;


public interface AdminInterface {
   
   UUID getAdminId();
   void setAdminId(UUID adminId);
   default boolean isAdmin(UUID userId) {
      return getAdminId() != null && getAdminId().equals(userId);
   }

}
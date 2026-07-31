
// packages
package com.example.ProjectFlow.common.interfaces.permissions;

// import uuid
import java.util.UUID;


public interface OwnableInterface {
 
   UUID getOwnerId();
   void setOwnerId(UUID ownerId);
   default boolean isOwner(UUID userId) {
      return getOwnerId() != null && getOwnerId().equals(userId);
   }

}
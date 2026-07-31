
// packages
package com.example.ProjectFlow.common.interfaces.permissions;

// import uuid
import java.util.UUID;


public interface AccessControlInterface<T> {

   enum Role { OWNER, ADMIN, MEMBER }
   boolean hasAccess(UUID userId, Role requiredRole);

}
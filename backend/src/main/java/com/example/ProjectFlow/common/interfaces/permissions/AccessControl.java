
// packages
package com.example.ProjectFlow.common.interfaces.permissions;

// import uuid
import java.util.UUID;


public interface AccessControl<T> {

   enum Role { OWNER, ADMIN, MEMBER }
   boolean hasAccess(UUID userId, Role requiredRole);

}
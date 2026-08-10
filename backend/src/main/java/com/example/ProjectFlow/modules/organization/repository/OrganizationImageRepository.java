
// packages
package com.example.ProjectFlow.modules.organization.repository;

// imports
import java.util.UUID;

// mongo imports
import org.springframework.data.mongodb.repository.MongoRepository;

// import document
import com.example.ProjectFlow.modules.organization.document.OrganizationImageDocument;


public interface OrganizationImageRepository extends MongoRepository<OrganizationImageDocument, String> {
 
   // find image by organization id
   OrganizationImageDocument findByOrganizationId(UUID organizationId);

   // delete image by organization id
   void deleteByOrganizationId(UUID organizationId);

   // exists by organization id
   boolean existsByOrganizationId(UUID organizationId);

}
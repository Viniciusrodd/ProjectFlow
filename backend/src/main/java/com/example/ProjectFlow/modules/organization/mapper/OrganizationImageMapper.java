
// packages
package com.example.ProjectFlow.modules.organization.mapper;

// imports
import org.springframework.stereotype.Component;

// import document
import com.example.ProjectFlow.modules.organization.document.OrganizationImageDocument;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationImageDTO.OrganizationImageResponseDTO;


@Component 
public class OrganizationImageMapper {
 
   // from OrganizationImageDocument to OrganizationImageResponseDTO
   public OrganizationImageResponseDTO toOrganizationImageResponseDTO(OrganizationImageDocument document) {
      return new OrganizationImageResponseDTO(
         document.getId(),
         document.getOrganizationId(),
         document.getFileName(),
         document.getMimeType(),
         document.getSize(),
         document.getUploadDate()
      );
   }

}

// packages
package com.example.ProjectFlow.modules.organization.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDeletedDTO;


@Component 
public class OrganizationMapper {
 
   // from OrganizationEntity to OrganizationResponseDTO
   public OrganizationResponseDTO toOrganizationResponseDTO(OrganizationEntity entity) {
      return new OrganizationResponseDTO(
         entity.getId(),
         entity.getOwnerId(),
         entity.getName(),
         entity.getDescription()
      );
   }


   // from OrganizationEntity to OrganizationDeletedDTO
   public OrganizationDeletedDTO toOrganizationDeletedDTO(OrganizationEntity entity) {
      return new OrganizationDeletedDTO(
         entity.getId(),
         entity.getOwnerId(),
         entity.getName(),
         entity.getDescription(),
         entity.getDeletedAt()
      );
   }

}
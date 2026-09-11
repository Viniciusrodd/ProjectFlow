
// packages
package com.example.ProjectFlow.modules.organization.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.MemberByOrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDeletedDTO;

// import mapper
import com.example.ProjectFlow.modules.user.mapper.UserMapper;


@Component 
public class OrganizationMembersMapper {

   // properties
   private final UserMapper userMapper;

   
   // constructor - dependency injection
   public OrganizationMembersMapper(UserMapper userMapper) {
      this.userMapper = userMapper;
   }

 
   // from OrganizationMembersEntity to OrganizationMembersResponseDTO
   public OrganizationMembersResponseDTO toOrganizationMembersResponseDTO(OrganizationMembersEntity entity) {
      return new OrganizationMembersResponseDTO(
         entity.getId(),
         entity.getOrganizationId(),
         entity.getUserId(),
         entity.getRole(),
         entity.getJoinedAt()
      );
   }


   // from OrganizationMembersEntity to MemberByOrganizationResponseDTO
   public MemberByOrganizationResponseDTO toMemberByOrganizationResponseDTO(OrganizationMembersEntity entity) {
      return new MemberByOrganizationResponseDTO(
         entity.getId(),
         this.userMapper.toUserProfileDTO(entity.getUser()),
         entity.getRole(),
         entity.getJoinedAt()
      );
   }


   // from OrganizationMembersEntity to OrganizationMembersDeletedDTO
   public OrganizationMembersDeletedDTO toOrganizationMembersDeletedDTO(OrganizationMembersEntity entity) {
      return new OrganizationMembersDeletedDTO(
         entity.getOrganizationId(),
         entity.getUserId(),
         entity.getRole(),
         entity.getJoinedAt(),
         entity.getDeletedAt()
      );
   }

}
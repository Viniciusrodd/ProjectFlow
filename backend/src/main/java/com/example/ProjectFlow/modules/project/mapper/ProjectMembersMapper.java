
// packages
package com.example.ProjectFlow.modules.project.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.MemberByProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDeletedDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersResponseDTO;

// import mapper
import com.example.ProjectFlow.modules.user.mapper.UserMapper;


@Component 
public class ProjectMembersMapper {

   // properties
   private final UserMapper userMapper;

   // constructor - dependency injection
   public ProjectMembersMapper(UserMapper userMapper) {
      this.userMapper = userMapper;
   }

 
   // from ProjectMembersEntity to ProjectMembersResponseDTO
   public ProjectMembersResponseDTO toProjectMembersResponseDTO(ProjectMembersEntity entity) {
      return new ProjectMembersResponseDTO(
         entity.getProjectId(),
         entity.getUserId(),
         entity.getRole(),
         entity.getJoinedAt()
      );
   }


   // from ProjectMembersEntity to MemberByProjectResponseDTO
   public MemberByProjectResponseDTO toMemberByProjectResponseDTO(ProjectMembersEntity entity) {
      return new MemberByProjectResponseDTO(
         entity.getId(),
         this.userMapper.toUserProfileDTO(entity.getUser()),
         entity.getRole(),
         entity.getJoinedAt()
      );
   }


   // from ProjectMembersEntity to ProjectMembersDeletedDTO
   public ProjectMembersDeletedDTO toProjectMembersDeletedDTO(ProjectMembersEntity entity) {
      return new ProjectMembersDeletedDTO(
         entity.getProjectId(),
         entity.getUserId(),
         entity.getRole(),
         entity.getJoinedAt(),
         entity.getDeletedAt()
      );
   }

}
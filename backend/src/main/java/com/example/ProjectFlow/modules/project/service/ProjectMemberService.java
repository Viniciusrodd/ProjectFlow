
// packages
package com.example.ProjectFlow.modules.project.service;

// imports
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.project.repository.ProjectMembersRepository;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectMembersValidator;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectMembersResponseDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectMembersCompleteResponseDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectMembersDTO;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.enums.RoleEnum;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Service
public class ProjectMemberService {
 
   // properties
   private final ProjectMembersRepository projectMembersRepository;
   private final ProjectMembersValidator projectMembersValidator;
   private final ProjectService projectService;
   private final UserService userService;

   // constructor - dependency injection
   public ProjectMemberService(
      ProjectMembersRepository projectMembersRepository,
      ProjectMembersValidator projectMembersValidator,
      UserService userService,
      @Lazy ProjectService projectService
   ) {
      this.projectMembersRepository = projectMembersRepository;
      this.projectMembersValidator = projectMembersValidator;
      this.userService = userService;
      this.projectService = projectService;
   }


   // create member participation
   @Transactional
   public ProjectMembersResponseDTO createMemberParticipation(ProjectMembersDTO data) {
      this.projectMembersValidator.userIdValidate(data.userId());
      this.projectMembersValidator.projectIdValidate(data.projectId());
      this.projectMembersValidator.roleValidate(data.role());

      // get user data
      UserEntity user = this.userService.getEntityById(data.userId());

      // get project data
      ProjectEntity project = this.projectService.getEntityById(data.projectId());

      return this.projectMembersRepository.createMemberParticipation(data, user, project);
   }


   // get all project members
   public List<ProjectMembersCompleteResponseDTO> getAllMembersByProjectId(UUID projectId) {
      this.projectMembersValidator.projectIdValidate(projectId);

      // project existence - check
      this.projectService.existsById(projectId);

      List<ProjectMembersCompleteResponseDTO> members = this.projectMembersRepository.getAllMembersByProjectId(projectId);
      
      if(members.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membros não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return members;
   }


   // get all members by role
   public List<ProjectMembersCompleteResponseDTO> getAllMembersByRole(UUID projectId, String role) {
      this.projectMembersValidator.projectIdValidate(projectId);
      this.projectMembersValidator.roleValidate(role);

      // project existence - check
      this.projectService.existsById(projectId);

      List<ProjectMembersCompleteResponseDTO> members = this.projectMembersRepository.getAllMembersByRole(projectId, RoleEnum.valueOf(role.toUpperCase()));
      
      if(members.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membros não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return members;
   }

}
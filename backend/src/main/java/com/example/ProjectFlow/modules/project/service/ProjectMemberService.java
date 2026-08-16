
// packages
package com.example.ProjectFlow.modules.project.service;

// imports
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.project.repository.ProjectMembersRepository;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectMembersValidator;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectMembersResponseDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectMembersDTO;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions

// import constants


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

}

// packages
package com.example.ProjectFlow.modules.project.service;

// imports
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDTO;
import com.example.ProjectFlow.modules.project.enums.RoleEnum;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectDTO;



@Service 
public class ProjectCreationService {
 
   // properties
   private final ProjectService projectService;
   private final ProjectMemberService projectMemberService;
   

   // constructor - dependency injection
   public ProjectCreationService(
      ProjectService projectService,
      ProjectMemberService projectMemberService
   ) {
      this.projectService = projectService;
      this.projectMemberService = projectMemberService;
   }

   
   // creation
   @Transactional
   public ProjectResponseDTO create(ProjectDTO data) {
      // project
      ProjectResponseDTO project = this.projectService.create(data);

      // project member - admin
      this.projectMemberService.createMemberParticipation(
         new ProjectMembersDTO(
            project.id(),
            data.ownerId(),
            RoleEnum.ADMIN.toString()
         )
      );

      return project;
   }


}
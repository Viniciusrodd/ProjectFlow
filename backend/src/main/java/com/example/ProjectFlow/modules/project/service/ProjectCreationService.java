
// packages
package com.example.ProjectFlow.modules.project.service;

// imports
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDTO;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;

// import service
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.project.enums.RoleEnum;

// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectMapper;


@Service 
public class ProjectCreationService {
 
   // properties
   private final ProjectService projectService;
   private final ProjectMemberService projectMemberService;
   private final ActivityLogService activityLogService;
   private final ProjectMapper projectMapper;
   

   // constructor - dependency injection
   public ProjectCreationService(
      ProjectService projectService,
      ProjectMemberService projectMemberService,
      ActivityLogService activityLogService,
      ProjectMapper projectMapper
   ) {
      this.projectService = projectService;
      this.projectMemberService = projectMemberService;
      this.activityLogService = activityLogService;
      this.projectMapper = projectMapper;
   }

   
   // creation
   @Transactional
   public ProjectResponseDTO create(ProjectDTO data) {
      // project
      ProjectEntity project = this.projectService.create(data);

      // project member - admin
      this.projectMemberService.createMemberParticipation(
         new ProjectMembersDTO(
            project.getId(),
            project.getOwnerId(),
            RoleEnum.ADMIN.toString()
         )
      );

      // activity log - registering
      ActivityLogDTO activityLog = new ActivityLogDTO(
         null, 
         project.getId(), 
         null, null, null,
         project.getOwnerId(),
         ActivityActionEnum.PROJETO_CRIADO,
         "Projeto: " + project.getName() + ", criado por: " + project.getOwner().getName()
      );
      this.activityLogService.create(activityLog);

      return this.projectMapper.toProjectResponseDTO(project);
   }


}
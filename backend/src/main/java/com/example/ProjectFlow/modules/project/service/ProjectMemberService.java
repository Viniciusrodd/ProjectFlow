
// packages
package com.example.ProjectFlow.modules.project.service;

// imports
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.transaction.Transactional;
import jakarta.persistence.NoResultException;

// import repository
import com.example.ProjectFlow.modules.project.repository.ProjectMembersRepository;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectMembersValidator;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.MemberByProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDeletedDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersResponseDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import enums
import com.example.ProjectFlow.modules.project.enums.RoleEnum;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectMembersMapper;


@Service
public class ProjectMemberService {
 
   // properties
   private final ProjectMembersRepository projectMembersRepository;
   private final ProjectMembersValidator projectMembersValidator;
   private final ProjectService projectService;
   private final UserService userService;
   private final ProjectMembersMapper projectMembersMapper;


   // constructor - dependency injection
   public ProjectMemberService(
      ProjectMembersRepository projectMembersRepository,
      ProjectMembersValidator projectMembersValidator,
      UserService userService,
      @Lazy ProjectService projectService,
      ProjectMembersMapper projectMembersMapper
   ) {
      this.projectMembersRepository = projectMembersRepository;
      this.projectMembersValidator = projectMembersValidator;
      this.userService = userService;
      this.projectService = projectService;
      this.projectMembersMapper = projectMembersMapper;
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

      // creation
      ProjectMembersEntity projectMembersEntity = this.projectMembersRepository.createMemberParticipation(data, user, project);
   
      return this.projectMembersMapper.toProjectMembersResponseDTO(projectMembersEntity);
   }


   // get all members
   public List<MemberByProjectResponseDTO> getAllProjectMembers() {
      List<ProjectMembersEntity> membersEntity = this.projectMembersRepository.getAllProjectMembers();

      if(membersEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membros não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<MemberByProjectResponseDTO> members = new ArrayList<>();
      for(ProjectMembersEntity member : membersEntity) {
         members.add(this.projectMembersMapper.toMemberByProjectResponseDTO(member));
      }

      return members;
   }


   // get member by relation id
   public MemberByProjectResponseDTO getProjectMemberById(UUID id) {
      this.projectMembersValidator.idValidate(id);

      try {
         ProjectMembersEntity projectMembersEntity = this.projectMembersRepository.getProjectMemberById(id);

         return this.projectMembersMapper.toMemberByProjectResponseDTO(projectMembersEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membro não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get all members by project
   public List<MemberByProjectResponseDTO> getAllMembersByProjectId(UUID projectId) {
      this.projectMembersValidator.projectIdValidate(projectId);

      // project existence - check
      this.projectService.existsById(projectId);

      // get members
      List<ProjectMembersEntity> membersEntity = this.projectMembersRepository.getAllMembersByProjectId(projectId);
      if(membersEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membros não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<MemberByProjectResponseDTO> members = new ArrayList<>();
      for(ProjectMembersEntity member : membersEntity) {
         members.add(this.projectMembersMapper.toMemberByProjectResponseDTO(member));
      }

      return members;
   }


   // get all members by role
   public List<MemberByProjectResponseDTO> getAllMembersByRole(UUID projectId, String role) {
      this.projectMembersValidator.projectIdValidate(projectId);
      this.projectMembersValidator.roleValidate(role);

      // project existence - check
      this.projectService.existsById(projectId);

      // get members
      List<ProjectMembersEntity> membersEntity = this.projectMembersRepository.getAllMembersByRole(projectId, RoleEnum.valueOf(role.toUpperCase()));
      
      if(membersEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membros não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<MemberByProjectResponseDTO> members = new ArrayList<>();
      for(ProjectMembersEntity member : membersEntity) {
         members.add(this.projectMembersMapper.toMemberByProjectResponseDTO(member));
      }

      return members;
   }


   // get entity by id
   public ProjectMembersEntity getEntityById(UUID id) {
      this.projectMembersValidator.idValidate(id);

      try {
         return this.projectMembersRepository.getEntityById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membro não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // exists by id
   public boolean existsById(UUID id) {
      this.projectMembersValidator.idValidate(id);

      boolean exist = this.projectMembersRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membro não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // exists by project and member id
   public boolean existsByProjectIdAndMemberId(UUID projectId, UUID memberId) {
      this.projectMembersValidator.projectIdValidate(projectId);
      this.projectMembersValidator.userIdValidate(memberId);

      boolean exist = this.projectMembersRepository.existsByProjectIdAndMemberId(projectId, memberId);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membro não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // check if user is a membership
   public boolean checkUserMembership(UUID userId, UUID projectId) {
      this.projectMembersValidator.userIdValidate(userId);
      this.projectMembersValidator.projectIdValidate(projectId);

      // user existence - check
      this.userService.existsById(userId);

      // project existence - check
      this.projectService.existsById(projectId);

      boolean exist = this.projectMembersRepository.checkUserMembership(userId, projectId);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não participa do projeto",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // count admins members by project
   public Long countAdminsByProject(UUID projectId) {
      this.projectMembersValidator.projectIdValidate(projectId);

      return this.projectMembersRepository.countAdminsByProject(projectId);
   }


   // last admin member by project - validate
   public void validateLastAdmin(UUID projectId) {
      Long admins = this.countAdminsByProject(projectId);

      if(admins <= 1) {
         throw MultiExceptions.unauthorized(String.format(
            "%s: Um projeto deve ter pelo menos 1 administrador",
            ResponseMessages.UNAUTHORIZED
         ));
      }
   }


   // update member role
   @Transactional
   public ProjectMembersResponseDTO updateMemberRole(UUID id, String role) {
      this.projectMembersValidator.idValidate(id);
      this.projectMembersValidator.roleValidate(role);

      try {
         // last admin - check
         ProjectMembersEntity member = this.getEntityById(id);
         if(member.getRole() == RoleEnum.ADMIN && !role.equalsIgnoreCase("ADMIN")) {
            this.validateLastAdmin(member.getProject().getId());
         }

         // update
         ProjectMembersEntity memberEntity = this.projectMembersRepository.updateMemberRole(id, RoleEnum.valueOf(role.toUpperCase()));

         return this.projectMembersMapper.toProjectMembersResponseDTO(memberEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Participação não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // remove member participation
   @Transactional
   public ProjectMembersDeletedDTO removeParticipation(UUID id) {
      this.projectMembersValidator.idValidate(id);

      try {
         // last admin - check
         ProjectMembersEntity member = this.getEntityById(id);
         if(member.getRole() == RoleEnum.ADMIN) {
            this.validateLastAdmin(member.getProject().getId());
         }

         // remove
         ProjectMembersEntity memberEntity = this.projectMembersRepository.removeParticipation(id);

         return this.projectMembersMapper.toProjectMembersDeletedDTO(memberEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Participação não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // is removed
   public boolean isRemoved(UUID id) {
      this.projectMembersValidator.idValidate(id);

      try {
         return this.projectMembersRepository.isRemoved(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Participação não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}
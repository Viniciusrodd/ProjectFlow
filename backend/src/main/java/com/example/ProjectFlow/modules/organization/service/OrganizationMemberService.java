
// packages
package com.example.ProjectFlow.modules.organization.service;

// imports
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.transaction.Transactional;
import jakarta.persistence.NoResultException;

// import repository
import com.example.ProjectFlow.modules.organization.repository.OrganizationMembersRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationMembersValidator;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.MemberByOrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDeletedDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersResponseDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import enums
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationMembersMapper;


@Service
public class OrganizationMemberService {

   // properties
   private final OrganizationMembersRepository organizationMembersRepository;
   private final OrganizationMembersValidator organizationMembersValidator;
   private final UserService userService;
   private final OrganizationService organizationService;
   private final ActivityLogService activityLogService;
   private final NotificationService notificationService;
   private final OrganizationMembersMapper organizationMembersMapper;
   

   // constructor - dependency injection
   public OrganizationMemberService(
      OrganizationMembersRepository organizationMembersRepository,
      OrganizationMembersValidator organizationMembersValidator,
      UserService userService,
      OrganizationService organizationService,
      ActivityLogService activityLogService,
      NotificationService notificationService,
      OrganizationMembersMapper organizationMembersMapper
   ) {
      this.organizationMembersRepository = organizationMembersRepository;
      this.organizationMembersValidator = organizationMembersValidator;
      this.userService = userService;
      this.organizationService = organizationService;
      this.activityLogService = activityLogService;
      this.notificationService = notificationService;
      this.organizationMembersMapper = organizationMembersMapper;
   }


   // create member participation
   @Transactional
   public OrganizationMembersResponseDTO createMemberParticipation(OrganizationMembersDTO data) {
      this.organizationMembersValidator.userIdValidate(data.userId());
      this.organizationMembersValidator.organizationIdValidate(data.organizationId());
      this.organizationMembersValidator.roleValidate(data.role());

      // get user data
      UserEntity user = this.userService.getEntityById(data.userId());
      
      // get organization data
      OrganizationEntity organization = this.organizationService.getEntityById(data.organizationId());

      // creation
      OrganizationMembersEntity organizationMembersEntity = this.organizationMembersRepository.createMemberParticipation(data, user, organization);
   
      // activity log - registering
      ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
         .organizationId(organizationMembersEntity.getOrganizationId())
         .userId(user.getId())
         .action(ActivityActionEnum.MEMBRO_ADICIONADO_A_ORGANIZACAO)
         .description(
            "Participação do usuário: " + organizationMembersEntity.getUser().getName() + 
            ", adicionada á organização: " + organizationMembersEntity.getOrganization().getName() + 
            ", como: " + organizationMembersEntity.getRole()
         )
         .build();

      this.activityLogService.create(activityLog);

      // notification - registering
      for(OrganizationMembersEntity notifyMember : organization.getMembers()) {
         NotificationDTO notification = new NotificationDTO.Builder()
            .userId(notifyMember.getUser().getId().toString())
            .title(NotificationTitleEnum.MEMBRO_ADICIONADO_A_ORGANIZACAO)
            .message("O usuário: " + user.getName() + ", foi adicionado á organização: " + organization.getName())
            .build();

         this.notificationService.create(notification);
      } 

      return this.organizationMembersMapper.toOrganizationMembersResponseDTO(organizationMembersEntity);
   }


   // get all members
   public List<MemberByOrganizationResponseDTO> getAllOrganizationMembers() {
      List<OrganizationMembersEntity> membersEntity = this.organizationMembersRepository.getAllOrganizationMembers();

      if(membersEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membros não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<MemberByOrganizationResponseDTO> members = new ArrayList<>();
      for(OrganizationMembersEntity member : membersEntity) {
         members.add(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(member));
      }

      return members;
   }


   // get member by relation id
   public MemberByOrganizationResponseDTO getOrganizationMemberById(UUID id) {
      this.organizationMembersValidator.idValidate(id);

      try {
         OrganizationMembersEntity organizationMembersEntity = this.organizationMembersRepository.getOrganizationMemberById(id);
      
         return this.organizationMembersMapper.toMemberByOrganizationResponseDTO(organizationMembersEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membro não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get all members by organization
   public List<MemberByOrganizationResponseDTO> getAllMembersByOrganizationId(UUID organizationId) {
      this.organizationService.existsById(organizationId);

      // get members
      List<OrganizationMembersEntity> membersEntity = this.organizationMembersRepository.getAllMembersByOrganizationId(organizationId);

      if(membersEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membros não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<MemberByOrganizationResponseDTO> members = new ArrayList<>();
      for(OrganizationMembersEntity member : membersEntity) {
         members.add(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(member));
      }

      return members;
   }


   // get member by organization id
   public MemberByOrganizationResponseDTO getMemberByOrganizationId(UUID userId, UUID organizationId) {
      this.userService.existsById(userId);
      this.organizationService.existsById(organizationId);

      try {
         OrganizationMembersEntity member = this.organizationMembersRepository.getMemberByOrganizationId(userId, organizationId);

         return this.organizationMembersMapper.toMemberByOrganizationResponseDTO(member);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membro não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get all members by role
   public List<MemberByOrganizationResponseDTO> getAllMembersByRole(
      UUID organizationId,
      String role
   ) {
      this.organizationService.existsById(organizationId);
      this.organizationMembersValidator.roleValidate(role);

      // get members
      List<OrganizationMembersEntity> membersEntity = this.organizationMembersRepository.getAllMembersByRole(organizationId, RoleEnum.valueOf(role.toUpperCase()));

      if(membersEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membros não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<MemberByOrganizationResponseDTO> members = new ArrayList<>();
      for(OrganizationMembersEntity member : membersEntity) {
         members.add(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(member));
      }

      return members;
   }


   // get entity by id
   public OrganizationMembersEntity getEntityById(UUID id) {
      this.organizationMembersValidator.idValidate(id);

      try {
         return this.organizationMembersRepository.getEntityById(id);
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
      this.organizationMembersValidator.idValidate(id);

      boolean exist = this.organizationMembersRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membro não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // exists by organization and member id
   public boolean existsByOrganizationIdAndMemberId(UUID organizationId, UUID memberId) {
      this.organizationMembersValidator.organizationIdValidate(organizationId);
      this.organizationMembersValidator.userIdValidate(memberId);

      boolean exist = this.organizationMembersRepository.existsByOrganizationIdAndMemberId(organizationId, memberId);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membro não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // check if user is a membership
   public boolean checkUserMembership(UUID userId, UUID organizationId) {
      this.userService.existsById(userId);      
      this.organizationService.existsById(organizationId);

      boolean exist = this.organizationMembersRepository.checkUserMembership(userId, organizationId);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não participa da organização",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // count admins members by organization
   public Long countAdminsByOrganization(UUID organizationId) {
      this.organizationMembersValidator.organizationIdValidate(organizationId);

      return this.organizationMembersRepository.countAdminsByOrganization(organizationId);
   }


   // last admin member by organization - validate
   public void validateLastAdmin(UUID organizationId) {
      long admins = this.countAdminsByOrganization(organizationId);

      if(admins <= 1) {
         throw MultiExceptions.unauthorized(String.format(
            "%s: Uma organização deve ter pelo menos 1 administrador",
            ResponseMessages.UNAUTHORIZED
         ));
      }
   }


   // update member role
   @Transactional
   public OrganizationMembersResponseDTO updateMemberRole(UUID id, String role) {
      this.organizationMembersValidator.idValidate(id);
      this.organizationMembersValidator.roleValidate(role); // not allow update to "owner"

      try {
         // last admin - check
         OrganizationMembersEntity member = this.getEntityById(id);
         if(member.getRole() == RoleEnum.ADMIN && !role.equalsIgnoreCase("ADMIN")) {
            this.validateLastAdmin(member.getOrganization().getId());
         }

         // update
         OrganizationMembersEntity memberEntity = this.organizationMembersRepository.updateMemberRole(id, RoleEnum.valueOf(role.toUpperCase()));
         
         // activity log - registering
         ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
            .organizationId(memberEntity.getOrganizationId())
            .userId(memberEntity.getUserId())
            .action(ActivityActionEnum.PAPEL_DO_MEMBRO_DA_ORGANIZACAO_ATUALIZADA)
            .description(
               "Papel do usuário: " + memberEntity.getUser().getName() + 
               ", da organização: " + memberEntity.getOrganization().getName() + 
               ", atualizado para: " + memberEntity.getRole()
            )
            .build();

         this.activityLogService.create(activityLog);

         // notification - registering
         for(OrganizationMembersEntity notifyMember : memberEntity.getOrganization().getMembers()) {
            NotificationDTO notification = new NotificationDTO.Builder()
               .userId(notifyMember.getUser().getId().toString())
               .title(NotificationTitleEnum.PAPEL_DO_MEMBRO_DA_ORGANIZACAO_ATUALIZADA)
               .message(
                  "Papel do usuário: " + memberEntity.getUser().getName() + 
                  ", foi atualizado para: " + memberEntity.getRole() + 
                  ", na organização: " + memberEntity.getOrganization().getName()
               )
               .build();
               
            this.notificationService.create(notification);
         }
         
         return this.organizationMembersMapper.toOrganizationMembersResponseDTO(memberEntity);
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
   public OrganizationMembersDeletedDTO removeParticipation(UUID id) {
      this.organizationMembersValidator.idValidate(id);

      try {
         // last admin - check
         OrganizationMembersEntity member = this.getEntityById(id);
         if(member.getRole() == RoleEnum.ADMIN) {
            this.validateLastAdmin(member.getOrganization().getId());
         }

         // remove
         OrganizationMembersEntity memberEntity = this.organizationMembersRepository.removeParticipation(id);

         // activity log - registering
         ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
            .organizationId(memberEntity.getOrganizationId())
            .userId(memberEntity.getUserId())
            .action(ActivityActionEnum.MEMBRO_REMOVIDO_DA_ORGANIZACAO)
            .description(
               "Participação do usuário: " + memberEntity.getUser().getName() + 
               ", removida da organização: " + memberEntity.getOrganization().getName()
            )
            .build();
            
         this.activityLogService.create(activityLog);

         // notification - registering
         for(OrganizationMembersEntity notifyMember : memberEntity.getOrganization().getMembers()) {
            NotificationDTO notification = new NotificationDTO.Builder()
               .userId(notifyMember.getUser().getId().toString())
               .title(NotificationTitleEnum.MEMBRO_REMOVIDO_DA_ORGANIZACAO)
               .message(
                  "Usuário: " + memberEntity.getUser().getName() +  
                  ", foi removido da organização: " + memberEntity.getOrganization().getName()
               )
               .build();
               
            this.notificationService.create(notification);
         }
      
         return this.organizationMembersMapper.toOrganizationMembersDeletedDTO(memberEntity);
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
      this.organizationMembersValidator.idValidate(id);

      try {
         return this.organizationMembersRepository.isRemoved(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Participação não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}
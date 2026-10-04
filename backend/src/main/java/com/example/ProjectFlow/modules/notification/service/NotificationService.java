
// packages
package com.example.ProjectFlow.modules.notification.service;

// imports
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.notification.repository.NotificationRepository;

// import services
import com.example.ProjectFlow.modules.user.service.UserService;

// import validator
import com.example.ProjectFlow.modules.notification.validator.NotificationValidator;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import document
import com.example.ProjectFlow.modules.notification.document.NotificationDocument;

// import DTO
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationResponseDTO;

// import mapper
import com.example.ProjectFlow.modules.notification.mapper.NotificationMapper;


@Service
public class NotificationService {
 
   // properties
   private final NotificationRepository notificationRepository;
   private final NotificationValidator notificationValidator;
   private final NotificationMapper notificationMapper;
   private final UserService userService;


   // constructor - dependency injection
   public NotificationService(
      NotificationRepository notificationRepository,
      NotificationValidator notificationValidator,
      NotificationMapper notificationMapper,
      UserService userService
   ) {
      this.notificationRepository = notificationRepository;
      this.notificationValidator = notificationValidator;
      this.notificationMapper = notificationMapper;
      this.userService = userService;
   }


   // notification creation
   @Transactional 
   public NotificationResponseDTO create(UUID userId, NotificationDTO data) {
      this.userService.existsById(userId);
      this.notificationValidator.titleValidate(data.title().toString());
      if(data.message() != null && !data.message().isEmpty()) this.notificationValidator.messageValidate(data.message());

      try {
         // document setup
         NotificationDocument document = new NotificationDocument.Builder()
            .userId(userId)
            .title(data.title())
            .message(data.message())
            .read(false)
            .createdAt(LocalDateTime.now())
            .build();

         // save document - mongodb
         NotificationDocument savedDocument = this.notificationRepository.save(document);

         return this.notificationMapper.toNotificationResponseDTO(savedDocument);
      }
      catch (DataAccessException error) {
         throw MultiExceptions.internal(String.format(
            "%s: Erro na criação da notificação: %s", 
            ResponseMessages.INTERNAL_ERROR,
            error.getMessage()
         ));
      }
   }


   // get notification by id
   public NotificationResponseDTO getById(String id) {
      this.notificationValidator.idValidate(id);

      NotificationDocument document = this.notificationRepository.findById(id).orElse(null);
      if(document == null) {
         throw MultiExceptions.internal(String.format(
            "%s: Notificação não existe", 
            ResponseMessages.NOT_FOUND
         ));
      }

      return this.notificationMapper.toNotificationResponseDTO(document);      
   }

}
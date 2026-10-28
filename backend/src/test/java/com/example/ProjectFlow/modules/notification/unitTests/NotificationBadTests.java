
// packages
package com.example.ProjectFlow.modules.notification.unitTests;

// imports
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataAccessResourceFailureException;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
 
// mockito imports
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.notification.repository.NotificationRepository;
 
// import validator
import com.example.ProjectFlow.modules.notification.validator.NotificationValidator;
 
// import document
import com.example.ProjectFlow.modules.notification.document.NotificationDocument;
 
// import DTOs
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
 
// import service
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
 
// import mapper
import com.example.ProjectFlow.modules.notification.mapper.NotificationMapper;

// import enums
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - NotificationService")
public class NotificationBadTests {
 
   // mock dependencies
   @Mock
   private NotificationRepository notificationRepository;
   @Mock
   private NotificationValidator notificationValidator;
   @Mock
   private NotificationMapper notificationMapper;
   @Mock
   private UserService userService;
 
   // real instance for inject all mock objects above
   @InjectMocks
   private NotificationService notificationService;


   @Test
   @DisplayName("create() - must throw when database fails")
   void create() {
      // id's - setup
      UUID userId = UUID.randomUUID();
 
      // DTO - setup
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(userId.toString())
         .title(NotificationTitleEnum.TAREFA_ATRIBUIDA)
         .message("notification message")
         .build();
 
      // stubs
      when(this.notificationRepository.save(any(NotificationDocument.class)))
         .thenThrow(new DataAccessResourceFailureException("falha no mongo"));
 
      // act and assert
      assertThatThrownBy(() -> this.notificationService.create(notificationDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Erro na criação da notificação: falha no mongo");
 
      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.notificationValidator, times(1)).titleValidate(notificationDTO.title().toString());
      verify(this.notificationValidator, times(1)).messageValidate(notificationDTO.message());
      verify(this.notificationRepository, times(1)).save(any(NotificationDocument.class));
 
      // no checks
      verifyNoInteractions(this.notificationMapper);
   }


   @Test
   @DisplayName("getById() - must throw notification not found")
   void getById() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
 
      // stubs
      when(this.notificationRepository.findById(notificationId)).thenReturn(Optional.empty());
 
      // act and assert
      assertThatThrownBy(() -> this.notificationService.getById(notificationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Notificação não existe");
 
      // checks
      verify(this.notificationValidator, times(1)).idValidate(notificationId);
      verify(this.notificationRepository, times(1)).findById(notificationId);
 
      // no checks
      verify(this.notificationRepository, never()).markAsRead(any());
      verifyNoInteractions(this.notificationMapper);
   }


   @Test
   @DisplayName("getAllByUserId() - must throw when notification list is empty")
   void getAllByUserId() {
      // id's - setup
      UUID userId = UUID.randomUUID();
 
      // list - setup
      List<NotificationDocument> documentList = List.of();
 
      // stubs
      when(this.notificationRepository.findAllByUserId(userId)).thenReturn(documentList);
 
      // act and assert
      assertThatThrownBy(() -> this.notificationService.getAllByUserId(userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Notificações não existem");
 
      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.notificationRepository, times(1)).findAllByUserId(userId);
 
      // no checks
      verify(this.notificationRepository, never()).markAsRead(any());
      verifyNoInteractions(this.notificationMapper);
   }


   @Test
   @DisplayName("existsById() - must throw when notification does not exist")
   void existsById() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
 
      // stubs
      when(this.notificationRepository.existsById(notificationId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.notificationService.existsById(notificationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Notificação não existe");
 
      // checks
      verify(this.notificationValidator, times(1)).idValidate(notificationId);
      verify(this.notificationRepository, times(1)).existsById(notificationId);
   }
 
 
   @Test
   @DisplayName("existsByUserId() - must throw when user has no notifications")
   void existsByUserId() {
      // id's - setup
      UUID userId = UUID.randomUUID();
 
      // stubs
      when(this.notificationRepository.existsByUserId(userId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.notificationService.existsByUserId(userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Notificação não existe");
 
      // checks
      verify(this.notificationValidator, times(1)).userIdValidate(userId);
      verify(this.notificationRepository, times(1)).existsByUserId(userId);
   }
 
 
   @Test
   @DisplayName("getDocumentById() - must throw notification not found")
   void getDocumentById() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
 
      // stubs
      when(this.notificationRepository.findById(notificationId)).thenReturn(Optional.empty());
 
      // act and assert
      assertThatThrownBy(() -> this.notificationService.getDocumentById(notificationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Notificação não existe");
 
      // checks
      verify(this.notificationValidator, times(1)).idValidate(notificationId);
      verify(this.notificationRepository, times(1)).findById(notificationId);
   }


   @Test
   @DisplayName("deleteAllByUserId() - must throw when notification list is empty")
   void deleteAllByUserIdEmptyList() {
      // id's - setup
      UUID userId = UUID.randomUUID();
 
      // list - setup
      List<NotificationDocument> documentList = List.of();
 
      // stubs
      when(this.notificationRepository.findAllByUserId(userId)).thenReturn(documentList);
 
      // act and assert
      assertThatThrownBy(() -> this.notificationService.deleteAllByUserId(userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Notificações não existem");
 
      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.notificationRepository, times(1)).findAllByUserId(userId);
 
      // no checks
      verify(this.notificationRepository, never()).deleteAllByUserId(any());
   }
 
 
   @Test
   @DisplayName("deleteNotification() - must throw when notification does not exist")
   void deleteNotification() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
 
      // stubs
      when(this.notificationRepository.findById(notificationId)).thenReturn(Optional.empty());
 
      // act and assert
      assertThatThrownBy(() -> this.notificationService.deleteNotification(notificationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Notificação não existe");
 
      // checks
      verify(this.notificationValidator, times(1)).idValidate(notificationId);
      verify(this.notificationRepository, times(1)).findById(notificationId);
 
      // no checks
      verify(this.notificationRepository, never()).deleteById(any());
   }

}
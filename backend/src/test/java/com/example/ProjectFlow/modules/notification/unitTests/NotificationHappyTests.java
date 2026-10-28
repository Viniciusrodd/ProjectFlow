
// packages
package com.example.ProjectFlow.modules.notification.unitTests;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
 
// mockito imports
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.notification.repository.NotificationRepository;
 
// import validator
import com.example.ProjectFlow.modules.notification.validator.NotificationValidator;
 
// import document
import com.example.ProjectFlow.modules.notification.document.NotificationDocument;
 
// import DTOs
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationResponseDTO;
 
// import service
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
 
// import mapper
import com.example.ProjectFlow.modules.notification.mapper.NotificationMapper;
 
// import enums
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - NotificationService")
public class NotificationHappyTests {
   
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
   @DisplayName("create() - must create a notification")
   void create() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
      UUID userId = UUID.randomUUID();
 
      // notification - setup
      NotificationDocument notificationDocument = new NotificationDocument.Builder()
         .id(notificationId)
         .userId(userId)
         .title(NotificationTitleEnum.TAREFA_ATRIBUIDA)
         .message("Tarefa: task x, atribuida ao projeto: project x")
         .read(false)
         .createdAt(LocalDateTime.now())
         .build();
 
      // DTO's - setup
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(userId.toString())
         .title(NotificationTitleEnum.TAREFA_ATRIBUIDA)
         .message("Tarefa: task x, atribuida ao projeto: project x")
         .build();
 
      NotificationResponseDTO notificationResponseDTO = new NotificationResponseDTO(
         notificationId, userId,
         notificationDocument.getTitle(), notificationDocument.getMessage(),
         notificationDocument.getRead()
      );
 
      // stubs
      when(this.notificationRepository.save(any(NotificationDocument.class))).thenReturn(notificationDocument);
      when(this.notificationMapper.toNotificationResponseDTO(notificationDocument)).thenReturn(notificationResponseDTO);
 
      // act and assert
      NotificationResponseDTO result = this.notificationService.create(notificationDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(notificationResponseDTO);
 
      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.notificationValidator, times(1)).titleValidate(notificationDTO.title().toString());
      verify(this.notificationValidator, times(1)).messageValidate(notificationDTO.message());
      verify(this.notificationRepository, times(1)).save(any(NotificationDocument.class));
      verify(this.notificationMapper, times(1)).toNotificationResponseDTO(notificationDocument);
   }


   @Test
   @DisplayName("turnNotificationAsRead() - must turn a notification as read")
   void turnNotificationAsRead() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
 
      // act
      assertThatCode(() -> this.notificationService.turnNotificationAsRead(notificationId))
         .doesNotThrowAnyException();
 
      // checks and assert (in this case)
      verify(this.notificationRepository, times(1)).markAsRead(notificationId);
   }
 
 
   @Test
   @DisplayName("getById() - must return notification by id and turn it as read")
   void getById() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
      UUID userId = UUID.randomUUID();
 
      // notification - setup
      NotificationDocument notificationDocument = new NotificationDocument.Builder()
         .id(notificationId)
         .userId(userId)
         .title(NotificationTitleEnum.TAREFA_ATRIBUIDA)
         .message("notification message")
         .read(false)
         .build();
 
      // DTO - setup
      NotificationResponseDTO notificationResponseDTO = new NotificationResponseDTO(
         notificationId, userId,
         notificationDocument.getTitle(), notificationDocument.getMessage(),
         notificationDocument.getRead()
      );
 
      // stubs
      when(this.notificationRepository.findById(notificationId)).thenReturn(Optional.of(notificationDocument));
      when(this.notificationMapper.toNotificationResponseDTO(notificationDocument)).thenReturn(notificationResponseDTO);
 
      // act and assert
      NotificationResponseDTO result = this.notificationService.getById(notificationId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(notificationResponseDTO);
 
      // checks
      verify(this.notificationValidator, times(1)).idValidate(notificationId);
      verify(this.notificationRepository, times(1)).findById(notificationId);
      verify(this.notificationRepository, times(1)).markAsRead(notificationId);
      verify(this.notificationMapper, times(1)).toNotificationResponseDTO(notificationDocument);
   }
 
 
   @Test
   @DisplayName("getAllByUserId() - must return all notifications by user id and turn them as read")
   void getAllByUserId() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
      UUID userId = UUID.randomUUID();
 
      // notification - setup
      NotificationDocument notificationDocument = new NotificationDocument.Builder()
         .id(notificationId)
         .userId(userId)
         .title(NotificationTitleEnum.TAREFA_ATRIBUIDA)
         .message("notification message")
         .read(false)
         .build();
 
      // list - setup
      List<NotificationDocument> documentList = List.of(notificationDocument);
 
      // DTO - setup
      NotificationResponseDTO notificationResponseDTO = new NotificationResponseDTO(
         notificationId, userId,
         notificationDocument.getTitle(), notificationDocument.getMessage(),
         notificationDocument.getRead()
      );
 
      // stubs
      when(this.notificationRepository.findAllByUserId(userId)).thenReturn(documentList);
      when(this.notificationMapper.toNotificationResponseDTO(notificationDocument)).thenReturn(notificationResponseDTO);
 
      // act and assert
      List<NotificationResponseDTO> result = this.notificationService.getAllByUserId(userId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(notificationResponseDTO);
 
      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.notificationRepository, times(1)).findAllByUserId(userId);
      verify(this.notificationRepository, times(1)).markAsRead(notificationId);
      verify(this.notificationMapper, times(1)).toNotificationResponseDTO(notificationDocument);
   }
 
 
   @Test
   @DisplayName("existsById() - must check if notification exist by id")
   void existsById() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
 
      // stubs
      when(this.notificationRepository.existsById(notificationId)).thenReturn(true);
 
      // act and assert
      boolean result = this.notificationService.existsById(notificationId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.notificationValidator, times(1)).idValidate(notificationId);
      verify(this.notificationRepository, times(1)).existsById(notificationId);
   }
 
 
   @Test
   @DisplayName("existsByUserId() - must check if notification exist by user id")
   void existsByUserId() {
      // id's - setup
      UUID userId = UUID.randomUUID();
 
      // stubs
      when(this.notificationRepository.existsByUserId(userId)).thenReturn(true);
 
      // act and assert
      boolean result = this.notificationService.existsByUserId(userId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.notificationValidator, times(1)).userIdValidate(userId);
      verify(this.notificationRepository, times(1)).existsByUserId(userId);
   }
 
 
   @Test
   @DisplayName("getDocumentById() - must return notification document by id")
   void getDocumentById() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
 
      // notification - setup
      NotificationDocument notificationDocument = new NotificationDocument.Builder()
         .id(notificationId)
         .build();
 
      // stubs
      when(this.notificationRepository.findById(notificationId)).thenReturn(Optional.of(notificationDocument));
 
      // act and assert
      NotificationDocument result = this.notificationService.getDocumentById(notificationId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(notificationDocument);
 
      // checks
      verify(this.notificationValidator, times(1)).idValidate(notificationId);
      verify(this.notificationRepository, times(1)).findById(notificationId);
   }
 
 
   @Test
   @DisplayName("deleteAllByUserId() - must delete all notifications by user id")
   void deleteAllByUserId() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
      UUID userId = UUID.randomUUID();
 
      // notification - setup
      NotificationDocument notificationDocument = new NotificationDocument.Builder()
         .id(notificationId)
         .userId(userId)
         .build();
 
      // list - setup
      List<NotificationDocument> documentList = List.of(notificationDocument);
 
      // stubs
      when(this.notificationRepository.findAllByUserId(userId)).thenReturn(documentList);
 
      // act
      assertThatCode(() -> this.notificationService.deleteAllByUserId(userId))
         .doesNotThrowAnyException();
 
      // checks and assert (in this case)
      verify(this.userService, times(1)).existsById(userId);
      verify(this.notificationRepository, times(1)).findAllByUserId(userId);
      verify(this.notificationRepository, times(1)).deleteAllByUserId(userId);
   }
 
 
   @Test
   @DisplayName("deleteNotification() - must delete a notification")
   void deleteNotification() {
      // id's - setup
      String notificationId = UUID.randomUUID().toString();
 
      // notification - setup
      NotificationDocument notificationDocument = new NotificationDocument.Builder()
         .id(notificationId)
         .build();
 
      // stubs
      when(this.notificationRepository.findById(notificationId)).thenReturn(Optional.of(notificationDocument));
 
      // act
      assertThatCode(() -> this.notificationService.deleteNotification(notificationId))
         .doesNotThrowAnyException();
 
      // checks and assert (in this case)
      verify(this.notificationValidator, times(1)).idValidate(notificationId);
      verify(this.notificationRepository, times(1)).findById(notificationId);
      verify(this.notificationRepository, times(1)).deleteById(notificationId);
   }

}
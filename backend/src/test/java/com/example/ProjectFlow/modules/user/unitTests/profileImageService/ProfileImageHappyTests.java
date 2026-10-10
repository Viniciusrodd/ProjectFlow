
// packages
package com.example.ProjectFlow.modules.user.unitTests.profileImageService;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// mockito imports
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// import document
import com.example.ProjectFlow.modules.user.document.ProfileImageDocument;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.profileImageDTO.ProfileImageResponseDTO;

// import mapper
import com.example.ProjectFlow.modules.user.mapper.ProfileImageMapper;

// import repositories
import com.example.ProjectFlow.modules.user.repository.ProfileImageRepository;

// import services
import com.example.ProjectFlow.modules.user.service.ProfileImageService;
import com.example.ProjectFlow.modules.user.service.UserService;

// import validators
import com.example.ProjectFlow.modules.user.validator.ProfileImageValidator;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - ProfileImageService")
public class ProfileImageHappyTests {
 
   // mock dependencies
   @Mock
   private ProfileImageRepository profileImageRepository;
   @Mock
   private UserService userService;
   @Mock
   private ProfileImageValidator profileImageValidator;
   @Mock
   private ProfileImageMapper profileImageMapper;

   // real instance for inject all mock objects above
   @InjectMocks  
   private ProfileImageService profileImageService;


   @Test
   @DisplayName("uploadProfileImage() - must upload a profile image")
   void uploadProfileImage() {
      // data
      UUID userId = UUID.randomUUID();
      byte[] binary = {1, 2, 3};

      // file - setup
      MultipartFile file = new MockMultipartFile(
         "file",
         "filename.png",
         "image/png",
         binary
      );

      // document - setup
      ProfileImageDocument savedDocument = new ProfileImageDocument.Builder()
         .id("1")
         .userId(userId)
         .fileName("filename.png")
         .mimeType("image/png")
         .size(3L)
         .uploadDate(LocalDateTime.now())
         .binary(binary)
         .build();

      // dto - setup
      ProfileImageResponseDTO expectedDto = new ProfileImageResponseDTO(
         "1", userId, "filename.png", "image/png", 3L, savedDocument.getUploadDate()
      );

      // mocks config
      when(this.userService.existsById(userId)).thenReturn(true);
      when(this.profileImageRepository.existsByUserId(userId)).thenReturn(true);
      // profileImageRepository.deleteByUserId -> void
      when(this.profileImageRepository.save(any(ProfileImageDocument.class))).thenReturn(savedDocument);
      //userService.updateProfileImageId -> void
      when(this.profileImageMapper.toProfileImageResponseDTO(savedDocument)).thenReturn(expectedDto);

      // act and assert
      ProfileImageResponseDTO result = this.profileImageService.uploadProfileImage(userId, file);
      assertThat(result)
         .isNotNull()
         .isEqualTo(expectedDto);

      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.profileImageValidator, times(1)).validate(file);
      verify(this.profileImageRepository, times(1)).existsByUserId(userId);
      verify(this.profileImageRepository, times(1)).deleteByUserId(userId);
      verify(this.profileImageRepository, times(1)).save(any(ProfileImageDocument.class));
      verify(this.userService, times(1)).updateProfileImageId(userId, "1");
      verify(this.profileImageMapper, times(1)).toProfileImageResponseDTO(savedDocument);      
   }


   @Test 
   @DisplayName("getProfileImage() - must get a profile user image")
   void getProfileImage() {
      // data
      UUID userId = UUID.randomUUID();
      byte[] binary = {1, 2, 3};

      // document - setup
      ProfileImageDocument document = new ProfileImageDocument.Builder()
         .id("1")
         .userId(userId)
         .fileName("filename.png")
         .mimeType("image/png")
         .size(3L)
         .uploadDate(LocalDateTime.now())
         .binary(binary)
         .build();

      // mocks config
      when(this.userService.existsById(userId)).thenReturn(true);
      when(this.profileImageRepository.findByUserId(userId)).thenReturn(document);

      // act and assert
      ProfileImageDocument result = this.profileImageService.getProfileImage(userId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(document);

      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.profileImageRepository, times(1)).findByUserId(userId);
   }


   @Test 
   @DisplayName("getAllProfileImages() - must return all profile images")
   void getAllProfileImages() {
      // data
      UUID userId = UUID.randomUUID();
      byte[] binary = {1, 2, 3};

      // document 1 - setup
      ProfileImageDocument doc1 = new ProfileImageDocument.Builder()
         .id("1")
         .userId(userId)
         .fileName("filename.png")
         .mimeType("image/png")
         .size(3L)
         .uploadDate(LocalDateTime.now())
         .binary(binary)
         .build();

      // document 2 - setup
      ProfileImageDocument doc2 = new ProfileImageDocument.Builder()
         .id("2")
         .userId(userId)
         .fileName("filename.png")
         .mimeType("image/png")
         .size(3L)
         .uploadDate(LocalDateTime.now())
         .binary(binary)
         .build();

      // list - setup
      List<ProfileImageDocument> documents = List.of(doc1, doc2);

      // mocks config
      when(this.profileImageRepository.findAll()).thenReturn(documents);

      // act and assert
      List<ProfileImageDocument> result = this.profileImageService.getAllProfileImages();
      assertThat(result)
         .isNotNull()
         .isEqualTo(documents);

      // checks
      verify(this.profileImageRepository, times(1)).findAll();
   }

}
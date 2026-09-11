
// packages
package com.example.ProjectFlow.modules.user.controller;

// imports
import java.util.UUID;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// swagger imports
import io.swagger.v3.oas.annotations.Operation;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import services
import com.example.ProjectFlow.modules.user.service.ProfileImageService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import document
import com.example.ProjectFlow.modules.user.document.ProfileImageDocument;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.profileImageDTO.ProfileImageResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import mapper
import com.example.ProjectFlow.modules.user.mapper.ProfileImageMapper;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH + "/users/profile-image/{userId}")
public class ProfileImageController {
 
   // properties
   private final ProfileImageService profileImageService;
   private final ProfileImageMapper profileImageMapper;

   // constructor - dependency injection
   public ProfileImageController(
      ProfileImageService profileImageService,
      ProfileImageMapper profileImageMapper
   ) {
      this.profileImageService = profileImageService;
      this.profileImageMapper = profileImageMapper;
   }


   // upload profile image
   @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   @Operation(summary = "Upload profile image")
   public ResponseEntity<ApiResponse<ProfileImageResponseDTO>> uploadProfileImage(
      @PathVariable UUID userId, 
      @RequestParam MultipartFile file
   ) {
      ProfileImageResponseDTO profileImageData = this.profileImageService.uploadProfileImage(userId, file);

      ApiResponse<ProfileImageResponseDTO> response = new ApiResponse.Builder<ProfileImageResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(profileImageData)
         .build();
      
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get profile image - infos
   @GetMapping()
   @Operation(summary = "Get profile image data informations")
   public ResponseEntity<ApiResponse<ProfileImageResponseDTO>> getProfileImage(@PathVariable UUID userId) {
      ProfileImageDocument profileImageDocument = this.profileImageService.getProfileImage(userId);
      ProfileImageResponseDTO profileImage = this.profileImageMapper.toProfileImageResponseDTO(profileImageDocument);

      ApiResponse<ProfileImageResponseDTO> response = new ApiResponse.Builder<ProfileImageResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(profileImage)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get profile image - download
   @GetMapping(
      value = "/download", 
      produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/webp", "image/jpg" }
   )
   @Operation(summary = "Download profile image")
   public ResponseEntity<byte[]> getProfileImageData(@PathVariable UUID userId) {
      ProfileImageDocument document = this.profileImageService.getProfileImage(userId);

      return ResponseEntity
         .ok()
         .contentType(MediaType.parseMediaType(document.getMimeType()))
         .header("Content-Disposition", "inline; filename=\"" + document.getFileName() + "\"")
         .body(document.getBinary());
   }


   // delete profile image
   @DeleteMapping()
   @Operation(summary = "Profile image delete")
   public ResponseEntity<ApiResponse<Void>> deleteProfileImage(@PathVariable UUID userId) {
      this.profileImageService.deleteProfileImage(userId);

      ApiResponse<Void> response = new ApiResponse.Builder<Void>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}
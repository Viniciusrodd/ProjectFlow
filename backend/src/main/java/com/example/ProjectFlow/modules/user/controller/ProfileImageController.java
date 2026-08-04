
// packages
package com.example.ProjectFlow.modules.user.controller;

// imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

// import entities
import com.example.ProjectFlow.modules.user.document.ProfileImagesDocument;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.ProfileImageResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH + "/users/profile-image")
public class ProfileImageController {
 
   // properties
   private ProfileImageService profileImageService;

   // constructor - dependency injection
   public ProfileImageController(ProfileImageService profileImageService) {
      this.profileImageService = profileImageService;
   }


   // upload profile image
   @PostMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public ResponseEntity<ApiResponse<ProfileImageResponseDTO>> uploadProfileImage(
      @PathVariable Long userId, 
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
   @GetMapping(value = "/{userId}")
   @Operation(summary = "Get profile image data informations")
   public ResponseEntity<ApiResponse<ProfileImageResponseDTO>> getProfileImage(@PathVariable Long userId) {
      ProfileImagesDocument profileImageDocument = this.profileImageService.getProfileImage(userId);
      ProfileImageResponseDTO profileImage = ProfileImageResponseDTO.get(profileImageDocument);

      ApiResponse<ProfileImageResponseDTO> response = new ApiResponse.Builder<ProfileImageResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.SUCCESS)
         .data(profileImage)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get profile image - download
   @GetMapping(
      value = "/{userId}/download", 
      produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/webp" }
   )
   @Operation(summary = "Download profile image")
   public ResponseEntity<byte[]> getProfileImageData(@PathVariable Long userId) {
      ProfileImagesDocument document = this.profileImageService.getProfileImage(userId);

      return ResponseEntity
         .ok()
         .contentType(MediaType.parseMediaType(document.getMimeType()))
         .header("Content-Disposition", "inline; filename=\"" + document.getFileName() + "\"")
         .body(document.getBinary());
   }


   // delete profile image
   @DeleteMapping(value = "/{userId}")
   @Operation(summary = "Profile image delete")
   public ResponseEntity<ApiResponse<Void>> deleteProfileImage(@PathVariable Long userId) {
      this.profileImageService.deleteProfileImage(userId);

      ApiResponse<Void> response = new ApiResponse.Builder<Void>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}
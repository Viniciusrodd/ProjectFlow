
// packages
package com.example.ProjectFlow.modules.user.controller;

// imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import services
import com.example.ProjectFlow.modules.user.service.ProfileImageService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

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
      @RequestBody MultipartFile file
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

}
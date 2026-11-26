
// import types
import { iApiRequest, iApiResponse, iApiFile } from "@shared/types/api/api.types";
import { iProfileImageMethods, ProfileImageResponseDTO } from "@renderer/src/modules/user/types/profileImageTypes/profileImage.types";

// uuid
export type UUID = string;


class ProfileImageService implements iProfileImageMethods {

   // upload
   public async uploadProfileImage(userId: UUID, file: File): Promise<iApiResponse<ProfileImageResponseDTO>> {
      const apiRequest: iApiRequest = {
         method: 'POST',
         path: `users/profile-image/${userId}`,
         params: file,
         private: true
      }

      return await window.api.request<ProfileImageResponseDTO>(apiRequest);
   }


   // get profile image
   public async getProfileImage(userId: UUID): Promise<iApiResponse<ProfileImageResponseDTO>> {
      const apiRequest: iApiRequest = {
         method: 'GET',
         path: `users/profile-image/${userId}`,
         private: true
      }

      return await window.api.request<ProfileImageResponseDTO>(apiRequest);
   }


   // get profile image - download
   public async getProfileImageData(userId: UUID): Promise<iApiResponse<iApiFile>> {
      const apiRequest: iApiRequest = {
         method: 'GET',
         path: `users/profile-image/${userId}/download`,
         private: true
      }

      return await window.api.requestFile(apiRequest);
   }


   // delete
   public async deleteProfileImage(userId: UUID): Promise<iApiResponse<void>> {
      const apiRequest: iApiRequest = {
         method: 'DELETE',
         path: `users/profile-image/${userId}`,
         private: true
      }

      return await window.api.request<ProfileImageResponseDTO>(apiRequest);
   }

}
export const profileImageService: ProfileImageService = new ProfileImageService();
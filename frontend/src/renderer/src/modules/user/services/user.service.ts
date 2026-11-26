
// import types
import { iApiRequest, iApiResponse } from "@shared/types/api/api.types";
import { 
   UserProfileDTO, UserUpdateDTO, 
   UserDeletedDTO, iUserMethods
} from "@renderer/src/modules/user/types/userTypes/user.types";

// uuid
export type UUID = string;


class UserService implements iUserMethods {

   // get all
   public async getAllUsers(): Promise<iApiResponse<UserProfileDTO[]>> {
      const apiRequest: iApiRequest = {
         method: 'GET',
         path: 'users',
         private: true
      };

      return await window.api.request<UserProfileDTO[]>(apiRequest);
   }


   // get by id
   public async getById(id: UUID | null): Promise<iApiResponse<UserProfileDTO>> {
      const apiRequest: iApiRequest = {
         method: 'GET',
         path: `user/${id}`,
         private: true
      };

      return await window.api.request<UserProfileDTO>(apiRequest);
   }


   // update user
   public async updateUser(userId: UUID | null, data: UserUpdateDTO): Promise<iApiResponse<UserProfileDTO>> {
      const apiRequest: iApiRequest = {
         method: 'PUT',
         path: `user/${userId}`,
         body: data,
         private: true
      };

      return await window.api.request<UserProfileDTO>(apiRequest);
   }


   // delete user
   public async deleteUser(userId: UUID | null): Promise<iApiResponse<UserDeletedDTO>> {
      const apiRequest: iApiRequest = {
         method: 'DELETE',
         path: `user/${userId}`,
         private: true
      };

      return await window.api.request<UserProfileDTO>(apiRequest);
   }

}
export const userService: UserService = new UserService();
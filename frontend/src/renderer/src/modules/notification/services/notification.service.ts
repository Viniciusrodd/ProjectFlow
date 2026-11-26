
// import types
import { 
   NotificationDTO,
   NotificationResponseDTO,
   iNotificationMethods 
} from "@renderer/src/modules/notification/types/notification.types";
import { iApiRequest, iApiResponse } from "@shared/types/api/api.types";

// uuid
type UUID = string;


class NotificationService implements iNotificationMethods {

   // create
   public async create(data: NotificationDTO): Promise<iApiResponse<NotificationResponseDTO>> {
      const apiRequest: iApiRequest = {
         method: 'POST',
         path: `notification`,
         body: data,
         private: true
      }

      return await window.api.request<NotificationResponseDTO>(apiRequest);
   }


   // get by id
   public async getNotificationById(id: string): Promise<iApiResponse<NotificationResponseDTO>> {
      const apiRequest: iApiRequest = {
         method: 'GET',
         path: `notification/${id}`,
         private: true
      }

      return await window.api.request<NotificationResponseDTO>(apiRequest);
   }


   // get by user
   public async getUserNotifications(userId: UUID): Promise<iApiResponse<NotificationResponseDTO[]>> {
      const apiRequest: iApiRequest = {
         method: 'GET',
         path: `user/${userId}/notifications`,
         private: true
      }

      return await window.api.request<NotificationResponseDTO[]>(apiRequest);      
   }

   
   // delete all user notifications
   public async deleteUserNotifications(userId: UUID): Promise<iApiResponse<void>> {
      const apiRequest: iApiRequest = {
         method: 'DELETE',
         path: `user/${userId}/notifications`,
         private: true
      }

      return await window.api.request<void>(apiRequest);            
   }


   // delete single notification
   public async deleteNotification(id: string): Promise<iApiResponse<void>> {
      const apiRequest: iApiRequest = {
         method: 'DELETE',
         path: `notification/${id}`,
         private: true
      }

      return await window.api.request<void>(apiRequest);                  
   }

}
export const notificationService: NotificationService = new NotificationService();
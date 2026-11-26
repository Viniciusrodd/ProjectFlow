
// import types
import { iApiResponse } from "@shared/types/api/api.types";

// import enums
import { NotificationEnum } from "@renderer/src/modules/notification/enums/notification.enum";

// uuid
export type UUID = string;


// notification DTO
export interface NotificationDTO {
   userId: string;
   title: NotificationEnum;
   message: string;
}


// notification response DTO
export interface NotificationResponseDTO {
   id: string;
   userId: string;
   title: NotificationEnum;
   message: string;
   read: boolean;
}


// notification methods
export interface iNotificationMethods {

   create(data: NotificationDTO): Promise<iApiResponse<NotificationResponseDTO>>
   getNotificationById(id: string): Promise<iApiResponse<NotificationResponseDTO>>
   getUserNotifications(userId: UUID): Promise<iApiResponse<NotificationResponseDTO[]>>
   deleteUserNotifications(userId: UUID): Promise<iApiResponse<void>>
   deleteNotification(id: string): Promise<iApiResponse<void>>

}
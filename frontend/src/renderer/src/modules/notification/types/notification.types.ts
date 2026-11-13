
// import enums
import { NotificationEnum } from "@notification/enums/notification.enum";

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
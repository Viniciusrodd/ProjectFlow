
// imports
import { useState, useEffect } from "react";

// import services
import { notificationService } from "@renderer/src/modules/notification/services/notification.service";

// import types
import { NotificationResponseDTO } from "@renderer/src/modules/notification/types/notification.types";


const useUserNotifications = (userId: string) => {
   const [ notifications, setNotifications ] = useState<NotificationResponseDTO[]>([]);


   useEffect(() => {
      if (!userId) return;
      
      let active = true;

      const request = async () => {
         try {
            const response = await notificationService.getUserNotifications(userId);
            if (!active || !response.success || !response.data) return;

            setNotifications(response.data.filter(n => !n.read));
         }
         catch (error) {
            console.error('❌ useUserNotifications error: ', error);
         }
      };
      request();

      return () => {
         active = false;
         setNotifications([]);
      };
   }, [userId]);

   
   return notifications;
};

export default useUserNotifications;
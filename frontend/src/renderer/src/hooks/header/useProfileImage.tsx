
// imports
import { useState, useEffect } from "react";

// import services
import { profileImageService } from "@renderer/src/modules/user/services/profileImage.service";


const useProfileImage = (userId: string) => {
   const [ imageUrl, setImageUrl ] = useState<string>();


   useEffect(() => {
      if (!userId) return;

      let active = true;
      let objectUrl: string | undefined;
      
      const request = async () => {
         try {
            const response = await profileImageService.getProfileImageData(userId);
            if (!active || !response.success || !response.data) return;
   
            const { bytes, mimeType } = response.data;
            const blob = new Blob([new Uint8Array(bytes)], { type: mimeType });
            
            objectUrl = URL.createObjectURL(blob);
            setImageUrl(objectUrl);
         }
         catch (error) {
            console.error('❌ useProfileImage error: ', error);
         }
      };
      request();

      return () => {
         active = false;
         if (objectUrl) URL.revokeObjectURL(objectUrl);         
         setImageUrl(undefined);
      }
   }, [userId]);


   return imageUrl;
};

export default useProfileImage;
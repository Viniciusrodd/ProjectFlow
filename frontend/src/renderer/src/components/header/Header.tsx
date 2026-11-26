
// imports
import { useState, useContext, useEffect } from 'react';

// import styles
import styles from '@renderer/src/components/header/Header.module.css';

// import images
import logo_image from '@renderer/src/assets/images/app_icon.png';
import user_image from '@renderer/src/assets/images/user.png';

// import types
import { NotificationResponseDTO } from '@shared/types/notification/notification.types';

// import context
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';

// import services
import { profileImageService } from '@renderer/src/modules/user/services/profileImage.service';


const Header = () => {
   // states
   const [ notificationActive, setNotificationActive ] = useState<boolean>(true);
   const [ notifications, /*setNotifications*/ ] = useState<NotificationResponseDTO[]>([]);
   const [ imageUrl, setImageUrl ] = useState<string>();
   const { authUser } = useContext(AuthContext);


   // get profile image
   useEffect(() => {
      let active = true;
      let objectUrl: string | undefined;
      
      const request = async () => {
         if (!authUser.id) return;

         const response = await profileImageService.getProfileImageData(authUser.id);
         if (!active || !response.success || !response.data) return;

         const { bytes, mimeType } = response.data;
         const blob = new Blob([new Uint8Array(bytes)], { type: mimeType });
         
         objectUrl = URL.createObjectURL(blob);
         setImageUrl(objectUrl);
      };
      request();

      return () => {
         active = false;
         if (objectUrl) URL.revokeObjectURL(objectUrl);
         setImageUrl(undefined);
      }
   }, [authUser.id]);


   // tsx
   return (
      <div className={ styles.header_container }>
         <div className={ styles.logo_container }>
            <img src={ logo_image } alt="logo_image.png" />
            <h1>ProjectFlow</h1>
         </div>

         <div className={ styles.interact_container }>
            <div className={ styles.search_bar }>
               <span className='material-symbols-outlined'>search</span>
               <input 
                  type="text" placeholder='Pesquisar projetos, tarefas, membros de time...' 
               />
            </div>

            <div className={ styles.profile_infos }>
               <div className={ styles.notification } onClick={ () => setNotificationActive(false) }>
                  { notificationActive ? (
                     <span className='material-symbols-outlined'>notifications_active</span>
                  ) : (
                     <span className='material-symbols-outlined'>notifications</span>
                  ) }
                  <p>
                     { notifications.length }
                  </p>
               </div>

               <div className={ styles.profile }>
                  <div className={ styles.infos }>
                     <p>
                        { authUser.name.length <= 15 ? authUser.name : `${authUser.name.slice(0, 15)}...` }
                     </p>
                     <p className={ styles.email }>
                        { authUser.email.length <= 18 ? authUser.email : `${authUser.email.slice(0, 18)}...` }
                     </p>
                  </div>

                  <img src={ imageUrl ?? user_image } alt="user_profile" />
               </div>
            </div>
         </div>
      </div>
   );
};

export default Header;
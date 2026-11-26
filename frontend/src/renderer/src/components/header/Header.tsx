
// imports
import { useContext, useState } from 'react';

// import styles
import styles from '@renderer/src/components/header/Header.module.css';

// import images
import logo_image from '@renderer/src/assets/images/app_icon.png';
import user_image from '@renderer/src/assets/images/user.png';

// import context
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';

// import types
import { NotificationResponseDTO } from '@renderer/src/modules/notification/types/notification.types';

// import hooks
import useProfileImage from '@renderer/src/hooks/header/useProfileImage';
import useUserNotifications from '@renderer/src/hooks/header/useUserNotifications';


const Header = () => {
   // states
   const { authUser } = useContext(AuthContext);
   const profileImage = useProfileImage(authUser.id!);
   const notifications = useUserNotifications(authUser.id!);
   const [ seenNotifications, setSeenNotifications ] = useState<NotificationResponseDTO[]>();
   const hasNew = notifications.length > 0 && notifications !== seenNotifications;


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
               <button 
                  type='button'
                  className={ styles.notification } 
                  onClick={ () => setSeenNotifications(notifications) }
               >
                  <span className='material-symbols-outlined'>
                     { hasNew ? 'notifications_active' : 'notifications' }
                  </span>
                  <p>{ hasNew ? notifications.length : 0 }</p>
               </button>

               <div className={ styles.profile }>
                  <div className={ styles.infos }>
                     <p>
                        { authUser.name.length <= 15 ? authUser.name : `${authUser.name.slice(0, 15)}...` }
                     </p>
                     <p className={ styles.email }>
                        { authUser.email.length <= 18 ? authUser.email : `${authUser.email.slice(0, 18)}...` }
                     </p>
                  </div>

                  <img src={ profileImage ?? user_image } alt="user_profile" />
               </div>
            </div>
         </div>
      </div>
   );
};

export default Header;
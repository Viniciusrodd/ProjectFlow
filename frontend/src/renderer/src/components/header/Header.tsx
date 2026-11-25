
// imports
import { useState, useContext } from 'react';

// import styles
import styles from '@renderer/src/components/header/Header.module.css';

// import images
import logo_image from '@renderer/src/assets/images/app_icon.png';

// import types
import { NotificationResponseDTO } from '@renderer/src/modules/notification/types/notification.types';

// import context
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';


const Header = () => {
   // states
   const [ notificationActive, setNotificationActive ] = useState<boolean>(true);
   const [ notifications, /*setNotifications*/ ] = useState<NotificationResponseDTO[]>([]);
   const { authUser } = useContext(AuthContext);


   // tsx
   return (
      <div className={ styles.header_container }>
         <div className={ styles.logo_container }>
            <img src={ logo_image } alt="logo_image.png" />
            <h1>ProjectFlow</h1>
         </div>

         <div className={ styles.interact_container }>
            <div className={ styles.search_bar }>
               <span className='material-symbols-outlined tooltip' data-tooltip="Pesquisar">search</span>
               <input 
                  type="text" placeholder='Pesquisar projetos, tarefas, membros de time...' 
               />
            </div>

            <div className={ styles.profile_infos }>
               <div className={ styles.notification } onClick={ () => setNotificationActive(false) }>
                  { notificationActive ? (
                     <span className='material-symbols-outlined tooltip' data-tooltip="Notificações">notifications_active</span>
                  ) : (
                     <span className='material-symbols-outlined tooltip' data-tooltip="Notificações">notifications</span>
                  ) }
                  <p>
                     { notifications.length }
                  </p>
               </div>

               <div className={ styles.profile }>
                  <div className={ styles.infos }>
                     <p>{ authUser.name }</p>
                     <p className={ styles.email }>{ authUser.email }</p>
                  </div>

                  <img src="" alt="" />
               </div>
            </div>
         </div>
      </div>
   );
};

export default Header;
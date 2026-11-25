
// imports
import { useNavigate } from 'react-router-dom';
import { useContext } from 'react';

// import style
import styles from '@renderer/src/pages/welcome/Welcome.module.css';

// import context
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';


const WelcomePage = () => {
   // variables
   const navigate = useNavigate();
   const { authUser } = useContext(AuthContext);


   // tsx
   return (
      <div className={ styles.container }>
         <h1 className={ styles.welcome_msg }>
            Olá, { authUser.name } <span className='material-symbols-outlined tooltip'>waving_hand</span>
         </h1>
         <h1 className={ styles.title }>
            Bem vindo/a ao ProjectFlow
         </h1>
         <p className={ styles.subtitle }>
            Plataforma de gerencia de projetos e equipes
         </p>

         <button 
            type='button' 
            className={ styles.welcome_button }
            onClick={ () => navigate('/home') }
         >
            ir para página principal
         </button>
      </div>
   );
};

export default WelcomePage;
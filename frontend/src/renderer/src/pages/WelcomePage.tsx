
// imports
import { useNavigate } from 'react-router-dom';

// import style
import styles from '@renderer/src/styles/Welcome.module.css';


const WelcomePage = () => {
   // variables
   const navigate = useNavigate();


   // tsx
   return (
      <div className={ styles.container }>
         <h1 className={ styles.title }>
            Bem vindo/a ao ProjectFlow
         </h1>
         <p className={ styles.subtitle }>
            Plataforma de gerencia de projetos e equipes
         </p>

         <button 
            type='button' 
            className={ styles.welcome_button }
            onClick={ () => navigate('/organization') }
         >
            ir para minhas organizações
         </button>
      </div>
   );
};

export default WelcomePage;
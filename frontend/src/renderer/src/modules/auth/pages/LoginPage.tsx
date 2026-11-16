
// imports
import { useNavigate } from 'react-router-dom';

// import styles
import styles from '../styles/Auth.module.css';


const LoginPage = () => {
   // states
   const navigate = useNavigate();
   

   // tsx
   return (
      <section className={ styles.container }>
         <div className={ styles.data_container }>
            <h1>Login page</h1>
            
            <button onClick={ () => navigate('/register') }>
               Go to Register page
            </button>
         </div>
      </section>
   );
};

export default LoginPage;
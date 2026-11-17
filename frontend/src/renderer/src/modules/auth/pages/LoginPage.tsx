
// imports
import { useNavigate } from 'react-router-dom';
import { useState } from 'react'

// import styles
import styles from '@renderer/src/modules/auth/styles/Auth.module.css';

// import types
import { LoginDTO, LoginCleanResponseDTO } from '@renderer/src/modules/auth/types/loginTypes/login.types';


const LoginPage = () => {
   // states
   const navigate = useNavigate();
   const [ loginData, setLoginData ] = useState<LoginDTO>({
      email: '', password: ''
   });
   const [ result, setResult ] = useState<LoginCleanResponseDTO>({ 
      id: null, name:'', email: ''
   });
   const [showResult, setShowResult] = useState<boolean>(false);


   // login
   const handleLogin = async (): Promise<void> => {
      try {
         const response: LoginCleanResponseDTO = await window.auth.login(loginData);
   
         setResult(response);
         setShowResult(true);
      }
      catch (error) {
         console.error('error at handle login: ', error);
      }
   };


   // clean states
   const clean = () => {
      setLoginData({ email: '', password: '' });
      setResult({ id: null, name:'', email: '' });
      setShowResult(false);
   };
   

   // tsx
   return (
      <section className={ styles.container }>
         <div className={ styles.data_container }>
            <h1>Login page</h1>
            
            <input type="email" value={ loginData.email } placeholder='email' onChange={ e => setLoginData(prev => ({ ...prev, email: e.target.value })) } /> 
            <input type="password" value={ loginData.password } placeholder='senha' onChange={ e => setLoginData(prev => ({ ...prev, password: e.target.value })) } /> 
            
            <button onClick={ handleLogin }> Entrar </button> 
            <button onClick={ clean }> Limpar </button> 

         </div>

         <button onClick={ () => navigate('/register') }>
            Go to Register page
         </button>

         {showResult && ( 
            <ul>
               <li>nome: { result.name }</li>
               <li>email: { result.email }</li>
            </ul>
         )} 
      </section>
   );
};

export default LoginPage;
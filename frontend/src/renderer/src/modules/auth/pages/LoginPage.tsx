
// imports
import { useNavigate } from 'react-router-dom';
import { useState } from 'react'

// import styles
import styles from '@renderer/src/modules/auth/styles/Auth.module.css';

// import types
import { LoginDTO, LoginCleanResponseDTO } from '@renderer/src/modules/auth/types/loginTypes/login.types';
import { iApiResponse } from '@shared/types/api/api.types';


const LoginPage = () => {
   // states
   const navigate = useNavigate();
   const [ loginData, setLoginData ] = useState<LoginDTO>({
      email: '', password: ''
   });
   const [ result, setResult ] = useState<LoginCleanResponseDTO>({ 
      id: null, name:'', email: ''
   });
   const [ error, setError ] = useState<string>('');
   const [showResult, setShowResult] = useState<boolean>(false);


   // login
   const handleLogin = async (): Promise<void> => {
      try {
         const response: iApiResponse<LoginCleanResponseDTO> = await window.auth.login(loginData);
         if(!response.success) throw new Error(response.message);

         setResult(response.data!);
         setShowResult(true);
      }
      catch (error) {
         console.error('❌', error);

         const errorMessage = error instanceof Error ? error.message : String(error);

         setShowResult(true);
         setError(errorMessage);
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
               { error !== '' && (
                  <li>error: { error }</li>
               ) }
            </ul>
         )} 
      </section>
   );
};

export default LoginPage;
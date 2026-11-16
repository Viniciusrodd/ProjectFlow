
// imports
import { useNavigate } from 'react-router-dom';
import { useState } from 'react'

// import styles
import styles from '../styles/Auth.module.css';

// import types
import { LoginDTO, LoginResponseDTO } from '../types/loginTypes/login.types';


const LoginPage = () => {
   // states
   const navigate = useNavigate();
   const [ loginData, setLoginData ] = useState<LoginDTO>({
      email: '', password: ''
   });
   const [ result, setResult ] = useState<LoginResponseDTO>({ 
      id: null, name:'', email: '', token: '' 
   });
   const [showResult, setShowResult] = useState<boolean>(false);


   // login
   const handleLogin = async (): Promise<void> => {
      const response: LoginResponseDTO = await window.auth.login(loginData);

      setResult(response);
      setShowResult(true);
   };


   // clean states
   const clean = () => {
      setLoginData({ email: '', password: '' });
      setResult({ id: null, name:'', email: '', token: '' });
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

         {result && showResult && ( 
            <ul>
               <li>nome: { result.name }</li>
               <li>email: { result.email }</li>
            </ul>
         )} 
      </section>
   );
};

export default LoginPage;
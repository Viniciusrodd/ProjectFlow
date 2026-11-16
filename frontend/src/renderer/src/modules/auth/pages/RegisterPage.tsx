
// imports
import { useState } from 'react'

// import styles
import styles from '../styles/Auth.module.css';

// import types
import { RegisterDTO, RegisterResponseDTO } from '../types/registerTypes/register.types';


const RegisterPage = () => {
   // states
   const [ registerData, setRegisterData ] = useState<RegisterDTO>({
      name:'', email: '', password: ''
   });
   const [ result, setResult ] = useState<RegisterResponseDTO>({ 
      id: null, name:'', email: '', password: '', createdAt: '' 
   });
   const [showResult, setShowResult] = useState<boolean>(false);


   // tsx
   return (
      <section className={ styles.container }>
         <div className={ styles.data_container }>
            <h1>Register</h1>

            <input type="text" value={ registerData.name } placeholder='name' onChange={e => setRegisterData(prev => ({ ...prev, name: e.target.value })) } /> 
            <input type="email" value={ registerData.email } placeholder='email' onChange={e => setRegisterData(prev => ({ ...prev, email: e.target.value })) } /> 
            <input type="password" value={ registerData.password } placeholder='senha' onChange={e => setRegisterData(prev => ({ ...prev, password: e.target.value })) } /> 
            
            <button /*onClick={handleLogin}*/> Entrar </button> 
            <button /*onClick={clean}*/> Limpar </button> 
         </div> 
         
         {result && showResult && ( 
            <ul>
               <li>nome: { result.name }</li>
               <li>email: { result.email }</li>
            </ul>
         )} 
      </section>
   );
};

export default RegisterPage;
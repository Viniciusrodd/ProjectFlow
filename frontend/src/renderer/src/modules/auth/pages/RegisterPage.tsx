
// imports
import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

// import styles
import styles from '@renderer/src/modules/auth/styles/Auth.module.css';

// import types
import { RegisterDTO, RegisterResponseDTO } from '@renderer/src/modules/auth/types/registerTypes/register.types';
import { iApiRequest, iApiResponse } from '@shared/types/api/api.types';

// import context
import { LoadingContext } from '@renderer/src/shared/contexts/loading/loading.context';

// import components
import Loading from '@renderer/src/shared/components/ui/loading/Loading';


const RegisterPage = () => {
   // variables
   const [ registerData, setRegisterData ] = useState<RegisterDTO>({
      name:'', email: '', password: ''
   });
   const [ error, setError ] = useState<string>('');
   const [ message, setMessage ] = useState<string>('');
   const { loading, setLoading } = useContext(LoadingContext);
   const [ redirect, setRedirect ] = useState<boolean>(false);
   const navigate = useNavigate();


   // redirect
   useEffect(() => {
      if (!redirect) return;

      const timeout = setTimeout(() => {
         navigate('/login');
      }, 3000);

      return () => {
         setLoading(false);
         clearTimeout(timeout);
      };
   }, [redirect, navigate]);


   // register
   const handleForm = async (e: React.SubmitEvent<HTMLFormElement>) => {
      e.preventDefault();
      setLoading(true);

      // data setup
      const apiRequest: iApiRequest = {
         method: 'POST',
         path: 'auth/register',
         body: registerData,
         private: false
      };

      try {
         const response: iApiResponse<RegisterResponseDTO> = await window.api.request<RegisterResponseDTO>(apiRequest);
         if(!response.success) throw new Error(response.message);

         setMessage('Você será redirecionado...');
         setRedirect(true);
      }
      catch (error) {
         setLoading(false);
         console.error('❌', error);

         const errorMessage = error instanceof Error ? error.message : String(error);
         setError(errorMessage);
      }
   }


   // tsx
   return (
      <section className={ styles.container }>
         <form 
            onSubmit={ handleForm }
            method='post'
            className={ styles.form }
         >
            <h1>Registre-se</h1>

            <input type="text" value={ registerData.name } placeholder='name' onChange={e => setRegisterData(prev => ({ ...prev, name: e.target.value })) } /> 
            <input type="email" value={ registerData.email } placeholder='email' onChange={e => setRegisterData(prev => ({ ...prev, email: e.target.value })) } /> 
            <input type="password" value={ registerData.password } placeholder='senha' onChange={e => setRegisterData(prev => ({ ...prev, password: e.target.value })) } /> 
            
            { loading ? (
               <Loading />
            ) : (
               <>               
                  <button type='submit'>
                     Registrar
                  </button>

                  <a href="/login">
                     Já possuo registro
                  </a>
               </>
            ) }
         </form> 
         
         { error !== '' && ( 
            <ul>
               <li>error: { error }</li>
            </ul>
         )}

         { message !== '' && (
            <p className={ styles.message }>{ message }</p>
         ) }
      </section>
   );
};

export default RegisterPage;
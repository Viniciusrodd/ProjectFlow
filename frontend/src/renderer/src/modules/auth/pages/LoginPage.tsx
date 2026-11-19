
// imports
import { Link } from 'react-router-dom';
import { useState, useContext } from 'react'

// import styles
import styles from '@renderer/src/modules/auth/styles/Auth.module.css';

// import types
import { LoginDTO } from '@renderer/src/modules/auth/types/loginTypes/login.types';

// import context
import { LoadingContext } from '@renderer/src/app/providers/loading/loading.context';
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';

// import components
import Loading from '@renderer/src/shared/components/ui/loading/Loading';


const LoginPage = () => {
   // states
   const [ loginData, setLoginData ] = useState<LoginDTO>({
      email: '', password: ''
   });
   const [ error, setError ] = useState<string>('');
   const [ message, setMessage ] = useState<string>('');
   const { loading, setLoading } = useContext(LoadingContext);
   const { login } = useContext(AuthContext);
   

   // login
   const handleForm = async (e: React.SubmitEvent<HTMLFormElement>): Promise<void> => {
      e.preventDefault();
      setLoading(true);
      setError('');
      setMessage('');
      
      try {
         await login(loginData);
         setLoading(false);
         setMessage('Você será redirecionado...');
      }
      catch (error) {
         setLoading(false);
         console.error('❌', error);

         const errorMessage = error instanceof Error ? error.message : String(error);
         setError(errorMessage);
      }
   };
   

   // tsx
   return (
      <section className={ styles.container }>
         <form 
            onSubmit={ handleForm }
            method='post'
            className={ styles.form }
         >
            <h1>Login</h1>
            
            <input type="email" value={ loginData.email } placeholder='email' onChange={ e => setLoginData(prev => ({ ...prev, email: e.target.value })) } /> 
            <input type="password" value={ loginData.password } placeholder='senha' onChange={ e => setLoginData(prev => ({ ...prev, password: e.target.value })) } /> 
            
            { loading ? (
               <Loading />
            ) : (
               <>
                  <button type='submit'>
                     Entrar
                  </button> 
                  
                  <Link to="/register">
                     Ainda não estou registrado
                  </Link>
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

export default LoginPage;
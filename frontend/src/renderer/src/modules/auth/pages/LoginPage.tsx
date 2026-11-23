
// imports
import { Link } from 'react-router-dom';
import { useState, useContext } from 'react'

// import styles
import styles from '@renderer/src/modules/auth/styles/Auth.module.css';

// import types
import { LoginDTO, LoginCleanResponseDTO } from '@renderer/src/modules/auth/types/loginTypes/login.types';
import { iApiResponse } from '@shared/types/api/api.types';
import { iModalConfig } from '@renderer/src/shared/types/modal.types';

// import context
import { LoadingContext } from '@renderer/src/app/providers/loading/loading.context';
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';

// import components
import Loading from '@renderer/src/components/loading/Loading';
import Modal from '@renderer/src/components/modal/Modal';


const LoginPage = () => {
   // states
   const [ loginData, setLoginData ] = useState<LoginDTO>({
      email: '', password: ''
   });
   const { loading, setLoading } = useContext(LoadingContext);
   const { login } = useContext(AuthContext);

   // modal
   const [ modal_display, setModal_display ] = useState<boolean>(false);
   const [ modal_title, setModal_title ] = useState<string>('');
   const [ modal_msg, setModal_msg ] = useState<string>('');
   const [ modal_btt, setmodal_btt ] = useState<boolean | string>(false);
   const [ modal_btt_2, setModal_btt_2 ] = useState<boolean | string>(false);


   // functions
   
   
   // modal config
   const modal_config = ({ title, msg, btt_event, btt_close, display }: iModalConfig) => {
      setModal_title(title ?? '');
      setModal_msg(msg ?? '');
      setmodal_btt(btt_event ?? false);
      setModal_btt_2(btt_close ?? false);
      setModal_display(display ?? false);
   };   

   // close modal
   const closeModal = () =>{
      modal_config({
         title: '', msg: '', btt_event: false, 
         btt_close: false, display: false
      });

      setLoading(false);
   };


   // login
   const handleForm = async (e: React.SubmitEvent<HTMLFormElement>): Promise<void> => {
      e.preventDefault();
      setLoading(true);
      
      try {
         const response: iApiResponse<LoginCleanResponseDTO> = await login(loginData);
         if (!response.success) throw new Error(response.message);

         setLoading(false);
      }
      catch (error) {
         setLoading(false);

         const errorMessage = error instanceof Error ? error.message : String(error);
         const title = errorMessage.split(':')[0];
         const message = errorMessage.split(':')[1];
         
         modal_config({
            title: `${ title } ❌`, 
            msg: `${ message }`, 
            btt_event: false, btt_close: 'Tentar novamente', display: true
         });
      }
   };
   

   // tsx
   return (
      <section className={ styles.container }>
         { /* modal */ }
         <Modal 
            title={ modal_title }
            msg={ modal_msg }
            btt_event={ modal_btt }
            btt_close={ modal_btt_2 }
            display={ modal_display }
            onClose={ closeModal }
         />

         <form 
            onSubmit={ handleForm }
            method='post'
            className={ styles.auth_form }
         >
            <h1 className={ styles.title }>
               Entre com sua conta
            </h1>
            
            <input type="email" value={ loginData.email } placeholder='email' required onChange={ e => setLoginData(prev => ({ ...prev, email: e.target.value })) } /> 
            <input type="password" value={ loginData.password } placeholder='senha' required onChange={ e => setLoginData(prev => ({ ...prev, password: e.target.value })) } /> 
            
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
      </section>
   );
};

export default LoginPage;
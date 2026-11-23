
// imports
import React, { useState, useContext, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';

// import styles
import styles from '@renderer/src/modules/auth/styles/Auth.module.css';

// import types
import { RegisterDTO, RegisterResponseDTO } from '@renderer/src/modules/auth/types/registerTypes/register.types';
import { iApiResponse } from '@shared/types/api/api.types';
import { iModalConfig } from '@renderer/src/shared/types/modal.types';

// import context
import { LoadingContext } from '@renderer/src/app/providers/loading/loading.context';

// import components
import Loading from '@renderer/src/components/loading/Loading';
import Modal from '@renderer/src/components/modal/Modal';

// import services
import { registerService } from '@renderer/src/modules/auth/services/register.service';


const RegisterPage = () => {
   // variables
   const [ data, setData ] = useState<RegisterDTO>({
      name:'', email: '', password: ''
   });
   const { loading, setLoading } = useContext(LoadingContext);
   const [ redirect, setRedirect ] = useState<boolean>(false);
   const navigate = useNavigate();

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


   // redirect
   useEffect(() => {
      if (!redirect) return;

      const clearMessage = setTimeout(() => {
         modal_config({
            title: '', msg: '', btt_event: false, 
            btt_close: false, display: false
         });

         navigate('/login');
      }, 3000);

      return () => {
         setLoading(false);
         clearTimeout(clearMessage);
      };
   }, [redirect, navigate]);


   // register
   const handleForm = async (e: React.SubmitEvent<HTMLFormElement>): Promise<void> => {
      e.preventDefault();
      setLoading(true);

      try {
         const response: iApiResponse<RegisterResponseDTO> = await registerService.register(data);
         if(!response.success) throw new Error(response.message);

         modal_config({
            title: 'Sucesso ✔️', 
            msg: `Registro feito com sucesso`, 
            btt_event: false, btt_close: false, display: true
         });

         setLoading(false);
         setRedirect(true);
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
   }


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
               Crie sua conta
            </h1>

            <input type="text" value={ data.name } placeholder='name' required onChange={e => setData(prev => ({ ...prev, name: e.target.value })) } /> 
            <input type="email" value={ data.email } placeholder='email' required onChange={e => setData(prev => ({ ...prev, email: e.target.value })) } /> 
            <input type="password" value={ data.password } placeholder='senha' required onChange={e => setData(prev => ({ ...prev, password: e.target.value })) } /> 
            
            { loading ? (
               <Loading />
            ) : (
               <>               
                  <button type='submit'>
                     Registrar
                  </button>

                  <Link to="/login">
                     Já possuo registro
                  </Link>
               </>
            ) }
         </form>
      </section>
   );
};

export default RegisterPage;
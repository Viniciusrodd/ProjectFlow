
// imports
import { useEffect, useState, type ReactNode } from 'react';

// import types
import { AuthStatus } from '@renderer/src/app/providers/auth/auth.context';
import { LoginDTO, LoginCleanResponseDTO } from "@shared/types/auth/login.types";
import { iApiResponse } from '@shared/types/api/api.types';
import { iAuthUser } from "@shared/types/auth/authUser.types";

// import context
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';


// types
type AuthPropsProvider = {
   children: ReactNode
};


// provider
export const AuthProvider = ({ children }: AuthPropsProvider) =>{
   // states
   const [ status, setStatus ] = useState<AuthStatus>('loading');
   const [ authUser, setAuthUser ] = useState<iAuthUser>({ id: '', email: '', name: '' });

   // is authenticated - check
   useEffect(() => {
      const authCheck = async () => {
         try{
            const isAuth: iAuthUser = await window.auth.isAuthenticated();

            if (isAuth.id !== null) {
               setStatus('authenticated');
               setAuthUser(isAuth);
            } else {
               setStatus('unauthenticated');
            }
         }
         catch (error) {
            setStatus('unauthenticated');
         }
      };
      authCheck();
   }, []);


   // login
   const login = async (data: LoginDTO): Promise<iApiResponse<LoginCleanResponseDTO>> => {
      const response: iApiResponse<LoginCleanResponseDTO> = await window.auth.login(data);
      
      if (response.success && response.data) {
         setStatus('authenticated');
         setAuthUser(response.data);
      } else {
         setStatus('unauthenticated');
      };

      return response;
   };

   
   // logout
   const logout = async (): Promise<boolean> => {
      const response: boolean = await window.auth.logout();
      if (response) {
         setStatus('unauthenticated');
         setAuthUser({ id: '', email: '', name: '' });
      } else {
         setStatus('authenticated');
      };

      return response;
   };


   return (
      <AuthContext.Provider value={{ status, login, logout, authUser }}>
         { children }
      </AuthContext.Provider>
   );
};
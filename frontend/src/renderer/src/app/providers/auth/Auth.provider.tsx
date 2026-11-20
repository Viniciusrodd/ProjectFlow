
// imports
import { useEffect, useState, type ReactNode } from 'react';

// import types
import { AuthStatus } from '@renderer/src/app/providers/auth/auth.context';
import { LoginDTO, LoginCleanResponseDTO } from "@shared/types/auth/login.types";
import { iApiResponse } from '@shared/types/api/api.types';

// import context
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';


// types
type AuthPropsProvider = {
   children: ReactNode
};


// provider
export const AuthProvider = ({ children }: AuthPropsProvider) =>{
   // states
   const [status, setStatus] = useState<AuthStatus>('loading');

   // is authenticated - check
   useEffect(() => {
      const authCheck = async () => {
         try{
            const isAuth = await window.auth.isAuthenticated();
            setStatus(isAuth ? 'authenticated' : 'unauthenticated');
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
      
      if (response.success) {
         setStatus('authenticated');
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
      } else {
         setStatus('authenticated');
      };

      return response;
   };


   return (
      <AuthContext.Provider value={{ status, login, logout }}>
         { children }
      </AuthContext.Provider>
   );
};
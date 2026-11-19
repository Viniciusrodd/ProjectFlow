
// imports
import { useEffect, useState, type ReactNode } from 'react';

// import types
import { AuthStatus } from '@renderer/src/app/providers/auth/auth.context';
import { LoginDTO } from "@shared/types/auth/login.types";

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
   const login = async (data: LoginDTO): Promise<void> => {
      await window.auth.login(data);
      setStatus('authenticated');
   };

   
   // logout
   const logout = async (): Promise<void> => {
      await window.auth.logout();
      setStatus('unauthenticated');
   };


   return (
      <AuthContext.Provider value={{ status, login, logout }}>
         { children }
      </AuthContext.Provider>
   );
};
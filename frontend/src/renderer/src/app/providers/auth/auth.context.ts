
// imports
import { createContext } from "react";

// import types
import { LoginDTO, LoginCleanResponseDTO } from "@shared/types/auth/login.types";
import { iApiResponse } from '@shared/types/api/api.types';

// types / interfaces
export type AuthStatus = 'loading' | 'authenticated' | 'unauthenticated';
interface AuthContextValue { 
   status: AuthStatus;
   login: (data: LoginDTO) => Promise<iApiResponse<LoginCleanResponseDTO>>;
   logout: () => Promise<boolean>; 
}

// context
export const AuthContext = createContext<AuthContextValue>({
   status: 'loading',
   login: async () => { throw new Error('AuthProvider not mounted'); },
   logout: async () => { throw new Error('AuthProvider not mounted'); },
});
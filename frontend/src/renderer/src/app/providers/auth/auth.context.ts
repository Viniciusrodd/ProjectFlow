
// imports
import { createContext } from "react";

// import types
import { LoginDTO, LoginCleanResponseDTO } from "@shared/types/auth/login.types";
import { iAuthUser } from "@shared/types/auth/authUser.types";
import { iApiResponse } from '@shared/types/api/api.types';

// types
export type AuthStatus = 'loading' | 'authenticated' | 'unauthenticated';

// interface
interface AuthContextValue { 
   status: AuthStatus;
   authUser: iAuthUser;
   login: (data: LoginDTO) => Promise<iApiResponse<LoginCleanResponseDTO>>;
   logout: () => Promise<boolean>; 
}

// context
export const AuthContext = createContext<AuthContextValue>({
   status: 'loading',
   authUser: { id: '', email: '', name: '' },
   login: async () => { throw new Error('AuthProvider not mounted'); },
   logout: async () => { throw new Error('AuthProvider not mounted'); },
});
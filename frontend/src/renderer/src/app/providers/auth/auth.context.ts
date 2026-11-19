
// imports
import { createContext } from "react";

// import types
import { LoginDTO } from "@shared/types/auth/login.types";

// types / interfaces
export type AuthStatus = 'loading' | 'authenticated' | 'unauthenticated';
interface AuthContextValue { 
   status: AuthStatus;
   login: (data: LoginDTO) => Promise<void>;
   logout: () => Promise<void>; 
}

// context
export const AuthContext = createContext<AuthContextValue>({
   status: 'loading',
   login: async () => { throw new Error('AuthProvider not mounted'); },
   logout: async () => { throw new Error('AuthProvider not mounted'); },
});

// imports
import { createContext } from "react";

// types / interfaces
export type AuthStatus = 'loading' | 'authenticated' | 'unauthenticated';
interface AuthContextValue { status: AuthStatus }

// context
export const AuthContext = createContext<AuthContextValue>({
   status: 'loading'
});
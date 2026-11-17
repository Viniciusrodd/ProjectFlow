
// import types
import { LoginDTO, LoginCleanResponseDTO } from '@shared/types/auth/login.types';

// auth
export interface iAuth {
   
   login(data: LoginDTO): Promise<LoginCleanResponseDTO>
   logout(): Promise<boolean>
   isAuthenticated(): Promise<boolean>
   
};
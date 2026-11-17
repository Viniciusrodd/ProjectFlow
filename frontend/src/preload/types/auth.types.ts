
// import types
import { LoginDTO, LoginCleanResponseDTO } from '@renderer/src/modules/auth/types/loginTypes/login.types';


// auth
export interface iAuth {
   
   login(data: LoginDTO): Promise<LoginCleanResponseDTO>
   logout(): Promise<boolean>
   isAuthenticated(): Promise<boolean>
   
};
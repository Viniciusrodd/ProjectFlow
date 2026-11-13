
// import types
import { LoginDTO, LoginResponseDTO } from '@renderer/src/modules/auth/types/loginTypes/login.types';


// auth
export interface iAuth {
   
   login(data: LoginDTO): Promise<LoginResponseDTO>
   logout(): Promise<boolean>
   isAuthenticated(): Promise<boolean>
   
};
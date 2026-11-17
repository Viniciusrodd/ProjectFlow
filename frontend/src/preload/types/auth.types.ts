
// import types
import { LoginDTO, LoginCleanResponseDTO } from '@shared/types/auth/login.types';
import { iApiResponse } from '@preload/types/api.types';

// auth
export interface iAuth {
   
   login(data: LoginDTO): Promise<iApiResponse<LoginCleanResponseDTO>>
   logout(): Promise<boolean>
   isAuthenticated(): Promise<boolean>
   
};
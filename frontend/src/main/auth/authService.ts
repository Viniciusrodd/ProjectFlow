
// import axios
import axios from "axios"; 

// import token store
import { TokenStore } from "./tokenStore";

// import interfaces
import { iAuth } from "@preload/types/auth.types";
import { LoginDTO, LoginResponseDTO, LoginCleanResponseDTO } from "@shared/types/auth/login.types";
import { iApiResponse } from "@preload/types/api.types";

// import env
import { ENV } from "@main/config/env";


export class AuthService implements iAuth {

   // properties
   tokenStore: TokenStore;


   constructor(tokenStore: TokenStore) {
      this.tokenStore = tokenStore;
   }


   // login
   public async login(data: LoginDTO): Promise<iApiResponse<LoginCleanResponseDTO>> {
      try {
         const res = await axios.post<iApiResponse<LoginResponseDTO>>(ENV.API_BASE_URL + "/auth/login", data);

         if (!res.data.success || !res.data.data?.token) {
            throw new Error(res.data.message ?? 'Credenciais inválidas.');
         }

         // save token
         await this.tokenStore.save(res.data.data!.token);

         return {
            success: res.data.success,
            statusCode: res.data.statusCode,
            message: res.data.message,
            data: {
               id: res.data.data!.id, name: res.data.data!.name, email: res.data.data!.email
            }
         }
      }
      catch (error) {
         if (axios.isAxiosError(error)) {
            return {
               success: false,
               statusCode: error.response!.status,
               message:
                  error.response?.data?.errorMessage ||
                  error.response?.data?.message ||
                  'Error at Login request',
            };
         }

         return { 
            success: false,
            statusCode: 500,
            message: 'Unexpected error' 
         };
      }
   }


   // logout
   public async logout(): Promise<boolean> {
      try {
         await this.tokenStore.clear();
         return true;
      }
      catch {
         return false;
      }
   }


   // is authenticated check
   public async isAuthenticated(): Promise<boolean> {
      return this.tokenStore.has();
   }

}

// import token store
import { TokenStore } from "./tokenStore";

// import interfaces
import { iAuth } from "@preload/types/auth.types";
import { LoginDTO, LoginResponseDTO, LoginCleanResponseDTO } from "@shared/types/auth/login.types";
import { iAuthUser } from "@shared/types/auth/authUser.types";
import { iApiResponse } from "@preload/types/api.types";

// import api client
import { ApiClient } from "@main/api/apiClient";


export class AuthService implements iAuth {

   // properties
   tokenStore: TokenStore;
   apiClient: ApiClient;


   constructor(tokenStore: TokenStore, apiClient: ApiClient) {
      this.tokenStore = tokenStore;
      this.apiClient = apiClient;
   }


   // login
   public async login(data: LoginDTO): Promise<iApiResponse<LoginCleanResponseDTO>> {
      const res: iApiResponse<LoginResponseDTO> = await this.apiClient.request<LoginResponseDTO>({
         method: 'POST',
         path: 'auth/login',
         body: data,
         private: false
      });

      if (!res.success || !res.data) {
         return {
            success: false,
            statusCode: res.statusCode,
            message: res.message ?? 'Login failed',
         };
      }

      if (!res.data.token) {
         return {
            success: false,
            statusCode: 500,
            message: 'Invalid response: missing token',
         };
      }

      // save token
      await this.tokenStore.save(res.data.token);

      return {
         success: res.success,
         statusCode: res.statusCode,
         message: res.message,
         data: {
            id: res.data!.id, 
            name: res.data!.name, 
            email: res.data!.email
         }
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
   public async isAuthenticated(): Promise<iAuthUser> {
      const res: iApiResponse<iAuthUser> = await this.apiClient.request<iAuthUser>({
         method: 'GET',
         path: 'auth/',
         private: true // passing safestorage loaded token throught headers authorization
      });

      if (!res.success || !res.data || res.statusCode === 401) {
         return { id: null, email: '', name: '' }
      };

      return res.data;
   }

}

// imports
import axios from "axios";

// import env
import { ENV } from "@main/config/env";

// import types
import { iApiRequest, iApiResponse, iApi } from "@preload/types/api.types";

// import token store
import { TokenStore } from "@main/auth/tokenStore";


export class ApiClient implements iApi {

   // properties
   tokenStore: TokenStore;
   
   // constructor
   constructor(tokenStore: TokenStore) {
      this.tokenStore = tokenStore;
   }


   async request<T = unknown>(req: iApiRequest): Promise<iApiResponse<T>> {
      // headers
      const headers: Record<string, string> = {
         'Content-Type': 'application/json'
      };
      
      try {         
         // get token - private endpoints
         if(req.private) {
            const token = await this.tokenStore.load();
            if (token == null) {
               return {
                  success: false,
                  statusCode: 401,
                  message: "Expired session, login again"
               };
            }
   
            headers.Authorization = `Bearer ${token}`;
         }
      
         // axios response
         const response = await axios({
            url: `${ENV.API_BASE_URL}/${req.path}`,
            method: req.method,
            headers: headers,
            data: req.body,
         });

         // response body - check
         const body = response.data as iApiResponse<T>;
         if(!body?.success) {
            return {
               success: false,
               statusCode: body?.statusCode ?? response.status,
               message: body?.message ?? 'Request failed',
               data: undefined,
            };
         }

         return {
            success: body.success,
            statusCode: body.statusCode,
            message: body.message,
            data: body.data ?? undefined
         };
      }
      catch (error) {
         if (axios.isAxiosError(error)) {
            return {
               success: false,
               statusCode: error.response?.status ?? 0,
               message:
                  error.response?.data?.errorMessage ||
                  error.response?.data?.message ||
                  error.message ||
                  'Error at Api request',
            }
         }

         return { 
            success: false,
            statusCode: 500,
            message: `Unexpected error: ${error instanceof Error ? error.message : String(error)}`
         };
      }
   };

} 
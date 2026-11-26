
// import types
import { iApiRequest, iApiResponse } from "@shared/types/api/api.types";
import {
   RegisterDTO, RegisterResponseDTO, iRegisterMethods 
} from "@renderer/src/modules/auth/types/registerTypes/register.types";


class RegisterService implements iRegisterMethods {

   public async register(data: RegisterDTO): Promise<iApiResponse<RegisterResponseDTO>> {
      const apiRequest: iApiRequest = {
         method: 'POST',
         path: 'auth/register',
         body: data,
         private: false
      };

      return await window.api.request<RegisterResponseDTO>(apiRequest);
   }

}
export const registerService: RegisterService = new RegisterService();
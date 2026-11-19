
// imports
import { contextBridge, ipcRenderer } from "electron";

// import types
import { LoginDTO, LoginCleanResponseDTO } from '@shared/types/auth/login.types';
import { iApiResponse, iApiRequest, iApi } from '@preload/types/api.types';


// auth
contextBridge.exposeInMainWorld('auth', {

   login: (data: LoginDTO): Promise<iApiResponse<LoginCleanResponseDTO>> => ipcRenderer.invoke('auth:login', data),
   logout: (): Promise<boolean> => ipcRenderer.invoke('auth:logout'),
   isAuthenticated: (): Promise<boolean> => ipcRenderer.invoke('auth:isAuthenticated'),

});


// api request
const api: iApi = {

   request: <T = unknown>(config: iApiRequest): Promise<iApiResponse<T>> => 
      ipcRenderer.invoke('api:request', config
   )

};
contextBridge.exposeInMainWorld('api', api);
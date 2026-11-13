
// imports
import { contextBridge, ipcRenderer } from "electron";

// import types
import { LoginDTO, LoginResponseDTO } from '@renderer/src/modules/auth/types/loginTypes/login.types';
import { iConfig, iApiResponse } from "@preload/types/api.types";


// auth
contextBridge.exposeInMainWorld('auth', {

   login: (data: LoginDTO): Promise<LoginResponseDTO> => ipcRenderer.invoke('auth:login', data),
   logout: (): Promise<boolean> => ipcRenderer.invoke('auth:logout'),

});


// api request
contextBridge.exposeInMainWorld('api', {

   request: (config: iConfig): Promise<iApiResponse> => ipcRenderer.invoke('api:request', config),

});
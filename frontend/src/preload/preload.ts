
// imports
import { contextBridge, ipcRenderer } from "electron";

// import types
import { LoginDTO, LoginCleanResponseDTO } from '@renderer/src/modules/auth/types/loginTypes/login.types';
//import { iConfig, iApiResponse } from "@preload/types/api.types";


// auth
contextBridge.exposeInMainWorld('auth', {

   login: (data: LoginDTO): Promise<LoginCleanResponseDTO> => ipcRenderer.invoke('auth:login', data),
   logout: (): Promise<boolean> => ipcRenderer.invoke('auth:logout'),
   isAuthenticated: (): Promise<boolean> => ipcRenderer.invoke('auth:isAuthenticated'),

});

/*
// api request
contextBridge.exposeInMainWorld('api', {

   request: (config: iConfig): Promise<iApiResponse> => ipcRenderer.invoke('api:request', config),

});
*/
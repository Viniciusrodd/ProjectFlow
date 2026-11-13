
// imports
import { ElectronAPI } from '@electron-toolkit/preload';

// import types
import { iAuth } from '@preload/types/auth.types';
import { iApi } from '@preload/types/api.types';


// advice for ts compiler about global variables 
declare global {
   interface Window {
      electron: ElectronAPI
      auth: iAuth,
      api: iApi
   }
}

// imports
import { ElectronAPI } from '@electron-toolkit/preload'


// advice for ts compiler about global variables 
declare global {
   interface Window {
      electron: ElectronAPI
      api: unknown
   }
}
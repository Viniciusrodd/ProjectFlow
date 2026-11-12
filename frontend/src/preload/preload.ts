
// imports
import { contextBridge, ipcRenderer } from "electron";


// electron api
contextBridge.exposeInMainWorld('electronAPI', {

   // test
   testFunction: (): Promise<string> => ipcRenderer.invoke('test')

});
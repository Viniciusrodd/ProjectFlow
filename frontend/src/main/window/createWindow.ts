
// imports
import { app, BrowserWindow } from 'electron';
import { join } from 'path';


// create window
export const createWindow = (width: number, height: number): BrowserWindow => {
   const window = new BrowserWindow({
      width,
      height,
      webPreferences: {
         preload: join(__dirname, '../../preload/preload.js'),
         contextIsolation: true,
         nodeIntegration: false,
         sandbox: true
      }
   });

   if (!app.isPackaged && process.env['ELECTRON_RENDERER_URL']) {
      window.loadURL(process.env['ELECTRON_RENDERER_URL']); // dev ambience: load from Vite server (with HMR)
   } else {
      window.loadFile(join(__dirname, '../../renderer/index.html')); // prod ambience: load final html file
   }

   return window;
};
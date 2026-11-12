
// imports
import { app, BrowserWindow, screen } from 'electron';
import { join } from 'path';


// window creation
function createWindow(): void {

   // get user primary monitor display
   const primaryDisplay = screen.getPrimaryDisplay();
   const { width, height } = primaryDisplay.workAreaSize;

   // window config
   const window = new BrowserWindow({
      width,
      height,
      webPreferences: {
         preload: join(__dirname, '../preload/index.js'),
         sandbox: false
      }
   });

   if(!app.isPackaged && process.env['ELECTRON_RENDERER_URL']) {
      window.loadURL(process.env['ELECTRON_RENDERER_URL']); // dev ambience: load from Vite server (with HMR)
   } else {
      window.loadFile(join(__dirname, '../renderer/index.html')); // prod ambience: load final html file
   }

};


// ready call
app.whenReady().then(() => {
   
   // create window
   createWindow();

});


// close all windows -> close app
app.on('window-all-closed', () => {
   if(process.platform !== 'darwin') app.quit();
});

// imports
import { app, screen, BrowserWindow } from 'electron';

// window imports
import { createWindow } from '@main/window/createWindow';

// ipc imports
import { AuthIpc } from './ipc/handlers/authIpcHandler';

// import token
import { TokenStore } from './auth/tokenStore';

// import services
import { AuthService } from './auth/authService';


class Main {

   // properties
   private tokenStore: TokenStore;
   private authService: AuthService;


   constructor() {
      this.tokenStore = new TokenStore();
      this.authService = new AuthService(this.tokenStore);
   }


   // start methods
   public start(): void {
      this.lifecycleHandlers();

      void app.whenReady().then(async () => {
         // load token
         await this.tokenStore.load();
   
         // ipc's
         new AuthIpc(this.authService).login();
         
         this.createWindow();
      });
   }


   // create window
   private createWindow(): void {
      const { width, height } = screen.getPrimaryDisplay().workAreaSize;
      createWindow(width, height);
   }


   // lifecycle handlers
   private lifecycleHandlers(): void {
      app.on('window-all-closed', () => {
         if (process.platform !== 'darwin') {
            app.quit()
         };
      });

      app.on('activate', () => {
         if (BrowserWindow.getAllWindows().length === 0) {
            this.createWindow()
         };
      });
   }

}


// start main
const main: Main = new Main();
main.start();
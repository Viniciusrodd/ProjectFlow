
// imports
import { app, screen, BrowserWindow } from 'electron';

// window imports
import { createWindow } from '@main/window/createWindow';

// ipc handlers imports
import { AuthIpc } from '@main/ipc/handlers/authIpcHandler';
import { ApiIpc } from '@main/ipc/handlers/apiIpcHandler';

// import token
import { TokenStore } from '@main/auth/tokenStore';

// import services
import { AuthService } from '@main/auth/authService';

// import api client
import { ApiClient } from "@main/api/apiClient";


class Main {

   // properties
   private tokenStore: TokenStore;
   private authService: AuthService;
   private apiClient: ApiClient;


   constructor() {
      this.tokenStore = new TokenStore();
      this.apiClient = new ApiClient(this.tokenStore);
      this.authService = new AuthService(this.tokenStore, this.apiClient);
   }


   // start methods
   public start(): void {
      this.lifecycleHandlers();

      void app.whenReady().then(async () => {
         // IPC handlers
         this.authIpcHandlers();
         this.apiIpcHandler();
         
         // window
         this.createWindow();
      });
   }


   // auth IPC handlers
   private authIpcHandlers(): void {
      new AuthIpc(this.authService).login();
      new AuthIpc(this.authService).logout();
      new AuthIpc(this.authService).isAuthenticated();
   }


   // api IPC handlers
   private apiIpcHandler(): void {
      new ApiIpc(this.apiClient).request();
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
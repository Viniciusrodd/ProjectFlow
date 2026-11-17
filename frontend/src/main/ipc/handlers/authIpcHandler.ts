
// imports
import { ipcMain } from "electron";

// import services
import { AuthService } from "@main/auth/authService";


export class AuthIpc {

   // properties
   authService: AuthService;

   constructor(authService: AuthService) {
      this.authService = authService;
   }


   // login
   public login() {
      ipcMain.handle('auth:login', async (_event, data) => {
         return await this.authService.login(data);
      });
   };

}
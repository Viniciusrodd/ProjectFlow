
// imports
import { app, screen, BrowserWindow } from 'electron';

// window imports
import { createWindow } from '@window/createWindow';


class Main {

   // properties
   private mainWindow: BrowserWindow | null = null;


   // start methods
   public start(): void {
      this.lifecycleHandlers();

      void app.whenReady().then(() => {
         this.createWindow()
      });
   }


   // create window
   private createWindow(): void {
      const { width, height } = screen.getPrimaryDisplay().workAreaSize;
      this.mainWindow = createWindow(width, height);

      console.log('✔️ main window: ', this.mainWindow.id);
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
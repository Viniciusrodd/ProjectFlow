
// imports
import { app, safeStorage } from 'electron';
import { promises as fs } from 'node:fs';
import path from 'node:path';

// import env
import { ENV } from '@main/config/env';


export class TokenStore {

   // properties
   private filePath: string;
   private cachedToken: string | null = null;

   
   constructor() {
      this.filePath = path.join(app.getPath('userData'), ENV.TOKEN_FILE_NAME); 
   }


   // save token
   public async save(token: string): Promise<void> {
      this.cachedToken = token;

      // safeStorage encryption OS service - check
      try {
         const isAvailable = await safeStorage.isAsyncEncryptionAvailable();
         if (!isAvailable) {
            console.warn('⚠️ [Save Token] safeStorage unavailable');
            return;
         }
   
         // encrypt token(binary) + save file
         const encrypted = await safeStorage.encryptStringAsync(token);
         await fs.mkdir(path.dirname(this.filePath), { recursive: true });
         await fs.writeFile(this.filePath, encrypted, { mode: 0o600 });

         console.log('✔️ token save in: ', this.filePath);
      }
      catch (error) {
         console.error('❌ Error at save token: ', error);
      }
   }


   // load token
   public async load(): Promise<boolean> {
      try {
         // get encrypt token(binary)
         const buffer = await fs.readFile(this.filePath);

         // safeStorage encryption OS service - check
         const isAvailable = await safeStorage.isAsyncEncryptionAvailable();
         if (!isAvailable) {
            console.warn('⚠️ [Load Token] safeStorage unavailable');
            return false;
         }

         // descrypt token + return true
         const { result } = await safeStorage.decryptStringAsync(buffer);
         this.cachedToken = result;
         
         if (result.length == 0) {
            console.warn("⚠️ [Load Token] token doesn't exist");
            return false;
         };

         console.log('✔️ token load from: ', this.filePath);
         return true;
      } 
      catch(error) {
         this.cachedToken = null;

         if (error instanceof Error && 'code' in error && error.code === 'ENOENT') {
            console.warn('⚠️ [Load Token] token file not found (ENOENT)');
         } else {
            console.error('❌ [Load Token] failed to load token:', error);
         }

         return false;
      }
   }


   // get token
   public get(): string | null {
      return this.cachedToken;
   }


   // check token existance
   public has(): boolean {
      return this.cachedToken !== null;
   }


   // clear token
   public async clear(): Promise<void> {
      this.cachedToken = null;

      try {
         await fs.unlink(this.filePath);
      } 
      catch(error) {
         if (error instanceof Error && 'code' in error && error.code === 'ENOENT') {
            console.warn('⚠️ [Clear Token] Token file does not exist, skipping deletion.');
         } else {
            console.error('❌ Error at clear token:', error);
            throw new Error('❌ Error at clear token', { cause: error });
         }
      }
   }

}
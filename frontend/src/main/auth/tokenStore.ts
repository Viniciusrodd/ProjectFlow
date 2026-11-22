
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
      if (typeof token !== 'string' || token.length === 0) {
         console.error('❌ [Save Token] invalid token received:', token);
         return;
      }

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
         throw error;
      }
   }


   // load token
   public async load(): Promise<string | null> {
      try {
         // get encrypt token(binary)
         const buffer = await fs.readFile(this.filePath);

         // safeStorage encryption OS service - check
         const isAvailable = await safeStorage.isAsyncEncryptionAvailable();
         if (!isAvailable) return null;

         // getting decrypted token
         const { result } = await safeStorage.decryptStringAsync(buffer);
         this.cachedToken = result;
         
         if (result.length == 0) return null;

         return this.cachedToken;
      } 
      catch(error) {
         this.cachedToken = null;

         return null;
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

// imports
import { resolve } from 'path'
import { defineConfig } from 'electron-vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
   main: {
      resolve: {
         alias: {
            '@main': resolve('src/main'),
            '@shared': resolve('src/shared'),
         }
      },
      plugins: [react()]
   },
   preload: {
      resolve: {
         alias: {
            '@preload': resolve('src/preload'),
            '@shared': resolve('src/shared'),
         }
      },
      plugins: [react()],
      build: {
         rollupOptions: {
            input: {
               preload: resolve(__dirname, 'src/preload/preload.ts')
            }
         }
      }
   },
   renderer: {
      resolve: {
         alias: {
            '@renderer': resolve('src/renderer'),            
            '@shared': resolve('src/shared'),
         }
      },
      plugins: [react()]
   }
});
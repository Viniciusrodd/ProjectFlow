
// imports
import { resolve } from 'path'
import { defineConfig } from 'electron-vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
   main: {
      resolve: {
         alias: {
            '@window': resolve('src/main/window')
         }
      },
      plugins: [react()]
   },
   preload: {},
   renderer: {
      resolve: {
         alias: {
            // modules
            '@auth': resolve('src/renderer/src/modules/auth')
         }
      },
      plugins: [react()]
   }
});
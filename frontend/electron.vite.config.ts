
// imports
import { resolve } from 'path'
import { defineConfig } from 'electron-vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
   main: {
      resolve: {
         alias: {
            '@main': resolve('src/main'),
            '@preload': resolve('src/preload'),            
            '@renderer': resolve('src/renderer')
         }
      },
      plugins: [react()]
   },
   preload: {},
   renderer: {
      resolve: {
         alias: {
            // modules
            '@activityLog': resolve('src/renderer/src/modules/activityLog'),
            '@attachment': resolve('src/renderer/src/modules/attachment'),
            '@auth': resolve('src/renderer/src/modules/auth'),
            '@board': resolve('src/renderer/src/modules/board'),
            '@comment': resolve('src/renderer/src/modules/comment'),
            '@labels': resolve('src/renderer/src/modules/labels'),
            '@notification': resolve('src/renderer/src/modules/notification'),
            '@organization': resolve('src/renderer/src/modules/organization'),
            '@project': resolve('src/renderer/src/modules/project'),
            '@task': resolve('src/renderer/src/modules/task'),
            '@user': resolve('src/renderer/src/modules/user'),
         }
      },
      plugins: [react()]
   }
});
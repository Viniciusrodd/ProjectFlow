
// import style
import '../src/styles/main.css';

// imports
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App'
import "@fontsource-variable/material-symbols-outlined";

// import providers
import { AuthProvider } from '@renderer/src/app/providers/auth/Auth.provider';
import { LoadingProvider } from '@renderer/src/app/providers/loading/Loading.provider';


createRoot(document.getElementById('root')!).render(
   <StrictMode>
      <AuthProvider>
         <LoadingProvider>
            <App />
         </LoadingProvider>
      </AuthProvider>
   </StrictMode>
);
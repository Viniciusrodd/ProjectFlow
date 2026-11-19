
// import style
import '../src/styles/main.css';

// imports
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App'

// import providers
import { LoadingProvider } from '@renderer/src/shared/contexts/loading/Loading.provider';


createRoot(document.getElementById('root')!).render(
   <StrictMode>
      <LoadingProvider>

         <App />
      
      </LoadingProvider>
   </StrictMode>
);
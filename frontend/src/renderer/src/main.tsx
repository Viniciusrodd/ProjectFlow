
// import style
import '../src/styles/main.css';

// imports
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App'

createRoot(document.getElementById('root')!).render(
   <StrictMode>
      <App />
   </StrictMode>
);
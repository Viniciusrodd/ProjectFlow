
// imports
import { HashRouter, Routes, Route, Navigate } from 'react-router-dom';

// import pages
import LoginPage from '@auth/pages/LoginPage';


function App() {
   return (
      <HashRouter>
         <Routes>
            {/* modules */}
            <Route path='/' element={<LoginPage />} />

            {/* 404 -> redirect to Login page */}
            <Route path="*" element={<Navigate to="/" />} />
         </Routes>
      </HashRouter>
   );
};

export default App;
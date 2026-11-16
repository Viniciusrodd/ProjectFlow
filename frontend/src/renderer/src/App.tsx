
// imports
import { HashRouter, Routes, Route, Navigate } from 'react-router-dom';

// import pages
import LoginPage from './modules/auth/pages/LoginPage';
import RegisterPage from './modules/auth/pages/RegisterPage';


function App() {
   return (
      <HashRouter>
         <Routes>
            {/* modules */}
            <Route path='/register' element={<RegisterPage />} />
            <Route path='/login' element={<LoginPage />} />

            {/* 404 -> redirect to Login page */}
            <Route path="*" element={<Navigate to="/login" />} />
         </Routes>
      </HashRouter>
   );
};

export default App;
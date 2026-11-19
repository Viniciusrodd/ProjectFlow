
// imports
import { HashRouter, Routes, Route, Navigate } from 'react-router-dom';

// import public pages
import LoginPage from '@renderer/src/modules/auth/pages/LoginPage';
import RegisterPage from '@renderer/src/modules/auth/pages/RegisterPage';

// import protected pages
import WelcomePage from '@renderer/src/pages/WelcomePage';

// import routes
import ProtectedRoute from '@renderer/src/app/router/ProtectedRoute';
import PublicRoute from '@renderer/src/app/router/PublicRoute';


function App() {
   return (
      <HashRouter>
         <Routes>
            {/* public - unauthenticated */}
            <Route element={ <PublicRoute /> }>
               <Route path='/register' element={ <RegisterPage /> } />
               <Route path='/login' element={ <LoginPage /> } />
            </Route>

            {/* protected - authenticated */}
            <Route element={ <ProtectedRoute /> }>
               <Route path='/welcome' element={ <WelcomePage /> } />
            </Route>

            {/* 404 */}
            <Route path="*" element={ <Navigate to="/login" replace /> } />
         </Routes>
      </HashRouter>
   );
};

export default App;
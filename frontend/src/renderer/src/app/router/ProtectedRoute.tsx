
// imports
import { useContext } from 'react';
import { Navigate, Outlet } from 'react-router-dom';

// import context
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';

// import components
import Loading from '@renderer/src/components/loading/Loading';


const ProtectedRoute = () => {
   // variables
   const { status } = useContext(AuthContext);

   if (status === 'loading') return <Loading />;
   if (status === 'unauthenticated') return <Navigate to="/login" replace />;

   // render next page/component
   return <Outlet />;
};

export default ProtectedRoute;
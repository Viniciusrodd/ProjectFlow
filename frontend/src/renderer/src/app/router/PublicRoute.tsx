
// imports
import { useContext } from 'react';
import { Navigate, Outlet } from 'react-router-dom';

// import context
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';

// import components
import Loading from '@renderer/src/shared/components/ui/loading/Loading';


const PublicRoute = () => {
   // variables
   const { status } = useContext(AuthContext);

   if (status === 'loading') return <Loading />;
   if (status === 'authenticated') return <Navigate to="/" replace />;

   // render next page/component
   return <Outlet />;
};

export default PublicRoute;
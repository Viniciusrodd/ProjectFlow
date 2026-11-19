
// imports
import { createContext } from "react";


// loading type
type LoadingContextType = {
   loading: boolean,
   setLoading: (loading: boolean) => void;
};

// export context
export const LoadingContext = createContext<LoadingContextType>({
   loading: false,
   setLoading: () => {}
});
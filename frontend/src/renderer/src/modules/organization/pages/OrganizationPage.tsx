
// imports
import { useState, useContext, useEffect } from 'react';

// import styles
import styles from '@renderer/src/modules/organization/styles/pages/OrganizationPage.module.css';

// import types
import { OrganizationResponseDTO } from '@renderer/src/modules/organization/types/organizationTypes/organization.types';
import { iApiResponse } from '@shared/types/api/api.types';

// import context
import { LoadingContext } from '@renderer/src/app/providers/loading/loading.context';

// import components
import Loading from '@renderer/src/shared/components/ui/loading/Loading';

// import services
import { organizationService } from '@renderer/src/modules/organization/services/organization.service';

// import components
import OrganizationComponent from '@renderer/src/modules/organization/components/OrganizationComponent';


const OrganizationPage = () => {
   // variables
   const [ error, setError ] = useState<string>('');
   const { loading, setLoading } = useContext(LoadingContext);
   const [ organizations, setOrganizations ] = useState<OrganizationResponseDTO[]>([]);


   // get all organizations
   useEffect(() => {
      const get = async () => {
         setLoading(true);

         try {
            const response: iApiResponse<OrganizationResponseDTO[]> = await organizationService.getAllOrganizations();
            if(!response.success) throw new Error(response.message);

            setLoading(false);
            setOrganizations(response.data ? response.data : []);
         }
         catch (error) {
            setLoading(false);
            console.error('❌', error);

            const errorMessage = error instanceof Error ? error.message : String(error);
            setError(errorMessage);
         }
      };

      get();
   }, []);


   // tsx
   return (
      <div className={ styles.container }>
         { loading ? (
            <Loading />
         ) : (
            <h1 className={ styles.title }>
               Organizações
            </h1>
         ) }

         { /* organizations */ }
         <div className={ `${styles.organization_container} scroll` }>
            { organizations.length > 0 && organizations.map(organization => (
               <OrganizationComponent 
                  id={ organization.id }
                  ownerId={ organization.ownerId }
                  name={ organization.name }
                  description={ organization.description }
               />
            )) }
         </div>

         { error !== '' && ( 
            <p className={ styles.message }>error: { error }</p>
         )}
      </div>
   );
};

export default OrganizationPage;
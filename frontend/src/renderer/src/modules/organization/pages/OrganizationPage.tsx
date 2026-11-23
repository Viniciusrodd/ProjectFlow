
// imports
import { useState, useContext, useEffect } from 'react';

// import styles
import styles from '@renderer/src/modules/organization/styles/pages/OrganizationPage.module.css';

// import types
import { OrganizationResponseDTO, OrganizationFormatedDTO } from '@renderer/src/modules/organization/types/organizationTypes/organization.types';
import { iApiResponse } from '@shared/types/api/api.types';
import { UserProfileDTO } from '@shared/types/user/user.types';

// import context
import { LoadingContext } from '@renderer/src/app/providers/loading/loading.context';
import { AuthContext } from '@renderer/src/app/providers/auth/auth.context';

// import components
import Loading from '@renderer/src/components/loading/Loading';

// import services
import { organizationService } from '@renderer/src/modules/organization/services/organization.service';
import { userService } from '@renderer/src/modules/user/services/user.service';

// import components
import OrganizationComponent from '@renderer/src/modules/organization/components/OrganizationComponent';


const OrganizationPage = () => {
   // variables
   const [ error, setError ] = useState<string>('');
   const [ organizations, setOrganizations ] = useState<OrganizationResponseDTO[]>([]);
   const [ organizationsFormatted, setOrganizationsFormatted ] = useState<OrganizationFormatedDTO[]>([]);
   const { loading, setLoading } = useContext(LoadingContext);
   const { authUser } = useContext(AuthContext);


   // get all organizations
   useEffect(() => {
      const get = async () => {
         setLoading(true);

         try {
            const response: iApiResponse<OrganizationResponseDTO[]> = await organizationService.getAllOrganizations();
            if(!response.success) throw new Error(response.message);

            setOrganizations(response.data ? response.data : []);
            setLoading(false);
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


   // format organizations
   useEffect(() => {
      if (organizations.length <= 0) {
         setOrganizationsFormatted([]);
         return;
      };

      let cancelled = false;
      
      const formatOrganizations = async () => {
         try {
            const formatted: OrganizationFormatedDTO[] = await Promise.all(
               organizations.map(async (organization) => {
                  const ownerName = await getOwner(organization.ownerId as string);

                  return {
                     id: organization.id,
                     ownerId: organization.ownerId,
                     ownerName,
                     name: organization.name,
                     description: organization.description,
                  };
               })
            );

            if (!cancelled) setOrganizationsFormatted(formatted);
         }
         catch (error) {
            if (!cancelled) {
               const errorMessage = error instanceof Error ? error.message : String(error);
               setError(errorMessage);
            }
         }
      }
      formatOrganizations();

      return () => { 
         cancelled = true; 
      };
   }, [organizations]);


   // get organization owner
   const getOwner = async (id: string): Promise<string> => {
      const response: iApiResponse<UserProfileDTO> = await userService.getById(id);
      if(!response.success || !response.data) throw new Error(response.message);

      return response.data.name;
   }   


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
            { organizationsFormatted.length > 0 && organizationsFormatted.map(organization => (
               <OrganizationComponent
                  key={ organization.id } 
                  id={ organization.id }
                  ownerId={ organization.ownerId }
                  ownerName={ organization.ownerName }
                  authId={ authUser.id }
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
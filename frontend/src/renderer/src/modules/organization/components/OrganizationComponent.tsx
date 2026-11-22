
// imports
import React from 'react';

// import styles
import styles from '@renderer/src/modules/organization/styles/components/OrganizationComponent.module.css';

// import types
import { iOrganizationComponent } from '@renderer/src/modules/organization/types/organizationTypes/organization.types';


const OrganizationComponent: React.FC<iOrganizationComponent> = ({
   id, ownerId, ownerName, authId, name, description
}) => {
   return (
      <div className={ styles.container } key={ id }>
         <h1>{ name }</h1>
         
         <p className={ styles.description }>{ description }</p>
         <p className={ styles.owner }>
            Criado por { authId == ownerId ? 'você' : ownerName }
         </p>

         { authId == ownerId && (
            <div className={ styles.buttons }>
               <span 
                  className={`material-symbols-outlined tooltip ${ styles.edit }`}
                  data-tooltip="Editar"
               > edit_square
               </span>
               <span 
                  className={`material-symbols-outlined tooltip ${ styles.delete }`}
                  data-tooltip="Deletar"
               > delete
               </span>
            </div>
         ) }
      </div>
   );
};

export default OrganizationComponent;
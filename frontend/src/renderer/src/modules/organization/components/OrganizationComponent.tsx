
// imports
import React from 'react';

// import styles
import styles from '@renderer/src/modules/organization/styles/components/OrganizationComponent.module.css';

// import types
import { OrganizationResponseDTO } from '@renderer/src/modules/organization/types/organizationTypes/organization.types';

// import images
import edit_png from '@renderer/src/assets/images/utils/edit.png';
import delete_png from '@renderer/src/assets/images/utils/delete.png';


const OrganizationComponent: React.FC<OrganizationResponseDTO> = ({
   id, ownerId, name, description
}) => {
   return (
      <div className={ styles.container } key={ id }>
         <h1>{ name }</h1>
         
         <p className={ styles.description }>{ description }</p>
         <p className={ styles.owner }>Criado por: { ownerId }</p>

         <div className={ styles.buttons }>
            <img src={ edit_png } alt="edit_png" />
            <img 
               src={ delete_png } alt="delete_png" 
               className='material-symbols-outlined tooltip'
               data-tooltip="Editar organização"
            />
         </div>
      </div>
   );
};

export default OrganizationComponent;
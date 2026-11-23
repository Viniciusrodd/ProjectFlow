
// imports
import React from 'react';

// import styles
import styles from '@renderer/src/components/modal/Modal.module.css';

// import types
import { iModal } from '@renderer/src/shared/types/modal.types';


const ModalComponent: React.FC<iModal> = ({ title, msg, btt_event, btt_close, btt_close_class, display, modalEvent, onClose }) => {
   return (
      <div className={ display ? styles.modal : styles.hidden }>
         <div className={ styles.modal_content }>
            <h2 className={styles.h2_modal }>
               { title }
            </h2>
            <p className={ styles.p_modal }>
               { msg }                
            </p>
            {
               btt_event && (
                  <button onClick={ modalEvent } type='button' className={ styles.modal_button }>
                     { btt_event }
                  </button>
               )
            }
            {
               btt_close && (
                  <button 
                     onClick={ onClose } 
                     type='button' 
                     className={ btt_close_class ? `${styles.green} ${styles.close_modal_button}` : `${styles.close_modal_button}` }
                  >
                     { btt_close }
                  </button>
               )
            }
         </div>
      </div>
   );
};

export default ModalComponent;
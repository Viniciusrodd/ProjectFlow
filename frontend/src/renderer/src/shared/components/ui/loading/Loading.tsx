
// import styles
import styles from '@renderer/src/shared/components/ui/loading/Loading.module.css';

// import images
import loading_img from '@renderer/src/assets/images/utils/loading.png';


const Loading = () => {
   return (
      <div className={ styles.loading_container }>
         <img 
            src={ loading_img } 
            alt="loading_png"
            className={ styles.loading_img }
         />

         <p className={ styles.loading_message }>
            carregando...
         </p>
      </div>
   );
};


export default Loading;
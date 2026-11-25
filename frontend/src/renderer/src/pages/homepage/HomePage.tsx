
// import styles
import styles from '@renderer/src/pages/homepage/HomePage.module.css';

// import components
import Header from '@renderer/src/components/header/Header';


const HomePage = () => {
   return (
      <div className={ styles.container }>
         <Header />

         <h1>Homepage</h1>    
      </div>
   );
};

export default HomePage;
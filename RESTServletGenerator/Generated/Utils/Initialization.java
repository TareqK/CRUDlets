package Utils;

import com.tna.DataAccess.Access;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener 
public class Initialization implements ServletContextListener {

        public Init(){
            
        }
    
        @Override
        public void contextInitialized(ServletContextEvent sce) {
           onInit();
           Access.connect();
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {
           onDestroy();
        }

    public void onInit() {
        Access.access.host = "localhost";
        Access.access.database = "test";
        Access.access.username = "root";
        Access.access.password = "root";
  
    }


    public void onDestroy() {
        
    }
    
}

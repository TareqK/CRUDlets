package utils;


import com.tna.data.Access;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tareq
 */

@WebListener 
public class Init implements ServletContextListener {

        public Init(){
            
        }
    
        @Override
        public void contextInitialized(ServletContextEvent sce) {
           onInit();
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {
           onDestroy();
        }

    public void onInit() {
        Access.host = "localhost";
        Access.database = "api_test";
        Access.username = "api_test";
        Access.password = "pass1234";
        Access.getInstance();
    }


    public void onDestroy() {
        
    }
    
}

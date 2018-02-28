/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Utils;

import com.tna.DataAccess.Access;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author tareq
 */
    @WebListener
    public abstract class ServiceInitializer implements ServletContextListener {


        @Override
        public void contextInitialized(ServletContextEvent sce) {
            
           onInit();
           Access.connect();
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {
           onDestroy();
        }
        
        public abstract void onInit();
        public abstract void onDestroy();
    }
       
      


    
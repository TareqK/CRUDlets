/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author tareq
 */
public abstract class Initialization implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        onInit();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        onDestroy();
    }

    /**
     * A function ran on servlet initialization.
     */
    public abstract void onInit();

    /**
     * A function run on servlet destruction.
     */
    public abstract void onDestroy();
}

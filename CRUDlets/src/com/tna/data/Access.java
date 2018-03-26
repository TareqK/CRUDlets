/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.data;

/**
 *
 * @author tareq
 */
public class Access {

    public static Access access = Access.getInstance();
    public static ConnectionPool pool;
    public static String host = "localhost:3306";
    public static String database = "test";
    public static String username = "root";
    public static String password = "pass1234";

    /**
     * A method to create the singleton data access class. If there is no instance, then create
     * a new instance of the class.
     * @return
     */
    public static Access getInstance() {
        if (access == null) {
            return new Access();
        } else {
            return access;
        }
    }
    

    /**
     * The default constructor. Returns a new instance of Access.
     */
    protected Access(){
        this.pool = new ConnectionPool();
    }
      


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.DataAccess;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author tareq
 */
public class Access {

    public static Access access = Access.getInstance();
    public static Connection connection = Access.connect();
    public String host = "localhost:3306";
    public String database = "test";
    public String username = "root";
    public String password = "pass1234";

    public static Access getInstance() {
        if (access == null) {
            return new Access();
        } else {
            return access;
        }
    }
    
    
    public static Connection connect() {
        if(Access.access.connection == null){
            
              System.out.println("no connection");
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String uri = "jdbc:mysql://" + Access.access.host + "/" + Access.access.database + "?" + "user=" + Access.access.username + "&password=" + Access.access.password + "";
                System.out.println(uri);
                Access.access.connection = DriverManager.getConnection(uri);

            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("fail");
                Access.access.connection = null;
            }
        }
        
        return Access.connection;
    }

    protected Access(){
    
    }
      


}

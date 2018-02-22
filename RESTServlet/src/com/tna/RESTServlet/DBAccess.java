/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.RESTServlet;

import java.sql.Connection;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author tareq
 */
public class DBAccess {

    public static DBAccess access = DBAccess.getInstance();
    public static Connection connection = DBAccess.connect();
    public static String host = "localhost";
    public static String database = "test";
    public static String username = "root";
    public static String password = "root";

    public static DBAccess getInstance() {
        if (access == null) {
            return new DBAccess();
        } else {
            return access;
        }
    }
    
    
    public static Connection connect() {
        DBAccess.getInstance();
        return DBAccess.connection;
    }

    protected DBAccess(){
        
        try{    
            Class.forName("com.mysql.jdbc.Driver");  
             String uri = "jdbc:mysql://"+DBAccess.host+
                "/"+DBAccess.database+"?"+"user="+DBAccess.username+"&password="+DBAccess.password+"";
            this.connection = DriverManager.getConnection(uri);
 
    } catch(SQLException |ClassNotFoundException e){
        this.connection = null;
    }

}
}

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
    public String host = "localhost:3306";
    public String database = "test";
    public String username = "root";
    public String password = "pass1234";

    public static DBAccess getInstance() {
        if (access == null) {
            return new DBAccess();
        } else {
            return access;
        }
    }
    
    
    public static Connection connect() {
        if(DBAccess.access.connection == null){
            
              System.out.println("no connection");
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String uri = "jdbc:mysql://" + DBAccess.access.host + "/" + DBAccess.access.database + "?" + "user=" + DBAccess.access.username + "&password=" + DBAccess.access.password + "";
                System.out.println(uri);
                DBAccess.access.connection = DriverManager.getConnection(uri);

            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("fail");
                DBAccess.access.connection = null;
            }
        }
        
        return DBAccess.connection;
    }

    protected DBAccess(){
        
        DBAccess access1 = DBAccess.access;
      

}
}

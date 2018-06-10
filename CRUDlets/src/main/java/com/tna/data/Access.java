/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author tareq
 */
public class Access {

    /**
     * The unique instance of the access class, which we use to set database
     * parameters and manage the connection pool.
     */
    public static Access access = Access.getInstance();

    /**
     * The connection pool we use to manage connections.
     */
    public static ConnectionPool pool;
    private static String host = "localhost:3306";
    private static String database = "test";
    private static String username = "root";
    private static String password = "pass1234";

    /**
     * Sets the database host.
     * @param host the host the database is on.
     */
    public static void setHost(String host) {
        Access.host = host;
    }

    /**
     * Sets the database we want to access.
     * @param database the database to connect to.
     */
    public static void setDatabase(String database) {
        Access.database = database;
    }

    /**
     * Sets the username of the database user.
     * @param username the database user we want to use to connect with.
     */
    public static void setUsername(String username) {
        Access.username = username;
    }

    /**
     * Sets the password of the database user.
     * @param password the password of the database user we want to use to connect with.
     */
    public static void setPassword(String password) {
        Access.password = password;
    }

    private static Access getInstance() {
        if (access == null) {
            return new Access();
        } else {
            return access;
        }
    }

    /**
     * Create a new database connection.
     * @return returns a new database connection, or null on failure.
     */
    public static Connection connect() {
        Connection conn;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String uri = "jdbc:mysql://" + Access.host + "/" + Access.database + "?" + "user=" + Access.username + "&password=" + Access.password + "&autoReconnect=true&useSSL=false";
            conn = DriverManager.getConnection(uri);

        } catch (SQLException | ClassNotFoundException e) {
            conn = null;
        }

        return conn;
    }

    /**
     * Create a new access and initialize an empty connection pool.
     */
    protected Access() {
        Access.pool = new ConnectionPool();
    }

}

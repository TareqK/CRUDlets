/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.data;

import com.tna.common.ObjectPool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author tareq
 */
public class ConnectionPool extends ObjectPool<Connection> {

    @Override
    protected Connection create() {
        Connection conn;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String uri = "jdbc:mysql://" + Access.host + "/" + Access.database + "?" + "user=" + Access.username + "&password=" + Access.password + "&autoReconnect=true&connectionTimeout=2000";
            conn = DriverManager.getConnection(uri);

        } catch (SQLException | ClassNotFoundException e) {
            conn = null;
        }

        return conn;
    }

}

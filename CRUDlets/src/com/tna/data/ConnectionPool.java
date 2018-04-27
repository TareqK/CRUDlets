/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.data;

import com.tna.common.ObjectPool;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author tareq
 */
public class ConnectionPool extends ObjectPool<Connection> {

    @Override
    protected Connection create() {
        return Access.connect();
    }

    @Override
    protected boolean isValid(Connection instance) {
        try {
            return instance.isValid(10);
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    protected void destroy(Connection instance) {
        try {
            instance.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

}

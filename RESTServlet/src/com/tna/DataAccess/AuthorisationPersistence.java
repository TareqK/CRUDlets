/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.DataAccess;

/**
 *
 * @author tareq
 */
public class AuthorisationPersistence extends Persistence {
    public static final String GET_PRIVILEGE_SQL = "SELECT privelege FROM %s WHERE token = ? ";
    public static final String GET_PASSWORD_SQL = "SELECT password FROM %s WHERE userName = ? ";
    public static final String SET_TOKEN_SQL = "UPDATE %s set token = ? where userName id = ? ";
}

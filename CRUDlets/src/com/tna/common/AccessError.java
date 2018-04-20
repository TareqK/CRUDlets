/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.common;

/**
 *
 * @author tareq
 */
public class AccessError extends Throwable {
    
    public enum ERROR_TYPE{
    ENTITY_NOT_FOUND,
    USER_NOT_AUTHORISED,
    USER_NOT_AUTHENTICATED,
    OPERATION_FAILED,
    ENTITY_UNAVAILABLE;
    
    }
    public ERROR_TYPE error ;
    
    public AccessError(ERROR_TYPE type){
        this.error=type;
    }
    
}

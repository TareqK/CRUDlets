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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Generic object pool
 *
 * @param <T> Type T of Object in the Pool
 */
public abstract class ObjectPool<T> {

    private Set<T> available = new HashSet<>();
    private Set<T> inUse = new HashSet<>();

    protected abstract T create();
    protected abstract void destroy(T instance);
    protected abstract boolean isValid(T instance);

    /**
     * Checkout object from pool
     */
    public synchronized T checkOut() {
        if (available.isEmpty()) {
            available.add(create());
        }
        T instance = available.iterator().next();
        available.remove(instance);
        if (isValid(instance)) {
            inUse.add(instance);
            return instance;
        } else {
            destroy(instance);
            return checkOut();
        }

    }

    public synchronized void checkIn(T instance) {
        inUse.remove(instance);
        if (isValid(instance)) {
            available.add(instance);
        } else {
            destroy(instance);
        }

    }

    public void initialize(int numberOfInstances) {
        ArrayList<T> instances = new ArrayList();
        for (int i = 0; i < numberOfInstances; i++) {
            instances.add(this.checkOut());
        }
        for (T instance : instances) {
            checkIn(instance);
        }
    }

    @Override
    public synchronized String toString() {
        return String.format("Pool available=%d inUse=%d", available.size(), inUse.size());
    }
}

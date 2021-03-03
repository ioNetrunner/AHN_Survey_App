/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ahn.authserver.resources;

/**
 *
 * @author rgustafs
 */
public class Employee {

    private Integer ID;
    private String username;

    public Employee() {
    }

    public Employee(Integer ID, String username) {
        this.ID = ID;
        this.username = username;
    }

    public Integer getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

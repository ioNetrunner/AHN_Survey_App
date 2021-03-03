/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.resources;

/**
 * Response to creating a new user
 *
 * @author rgustafs
 */
public class NewUser {

    private Integer ID;
    private String Username;
    private String Password;

    public NewUser(Integer ID, String Username, String Password) {
        this.ID = ID;
        this.Username = Username;
        this.Password = Password;
    }

    public Integer getID() {
        return ID;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

}

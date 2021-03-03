/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.resources;

/**
 *
 * @author rgustafs
 */
public class User {

    private Integer ID;
    private String Username;

    public User(Integer ID, String Username) {
        this.ID = ID;
        this.Username = Username;
    }

    public Integer getID() {
        return ID;
    }

    public String getUsername() {
        return Username;
    }

}

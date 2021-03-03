/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.resources;

/**
 *
 * @author rgustafs
 */
public class Options {

    private final Integer ID;
    private final String name;
    private final String lang;
    private final Integer version;

    public Options(Integer ID, String name, String lang, Integer version) {
        this.ID = ID;
        this.name = name;
        this.lang = lang;
        this.version = version;
    }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getLang() {
        return lang;
    }

    public Integer getVersion() {
        return version;
    }

}

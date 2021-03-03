/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author rgustafs
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
        reason = "Database update failed")
public class DatabaseUpdateException extends RuntimeException {

    public DatabaseUpdateException() {
    }

    public DatabaseUpdateException(String msg) {
        super(msg);
    }
}

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
@ResponseStatus(value = HttpStatus.FORBIDDEN,
        reason = "Cannot submit to that set")
public class InvalidAccessException extends RuntimeException {

    public InvalidAccessException() {
    }

    public InvalidAccessException(String msg) {
        super(msg);
    }
}

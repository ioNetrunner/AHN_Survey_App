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
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED,
        reason = "Previous active set must be terminated")
public class SetStillActiveException extends RuntimeException {

    public SetStillActiveException() {
    }

    public SetStillActiveException(String msg) {
        super(msg);
    }
}

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
@ResponseStatus(value=HttpStatus.GONE, 
        reason="Tried to terminate inactive or nonexistent set")
public class SetTerminationException extends RuntimeException {

    public SetTerminationException() {
    }

    public SetTerminationException(String msg) {
        super(msg);
    }
}

package org.bhmon.server.io.persistance.exceptions;

import org.bhmon.server.io.persistance.BHMonDAO_DB_MySQL;

public class BHMonDAOException extends RuntimeException {
    public BHMonDAOException(String message) {
        super(message);
    }
    public BHMonDAOException(String message, Throwable cause){super(message, cause);}
}

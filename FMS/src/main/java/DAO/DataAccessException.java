package DAO;

/** Catch an exception when accessing database */
public class DataAccessException extends Exception {
    DataAccessException(String message) {
        super(message);
    }

    DataAccessException() {
        super();
    }
}

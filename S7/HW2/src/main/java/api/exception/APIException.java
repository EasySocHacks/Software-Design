package api.exception;

import api.API;

/**
 * Global exception occurred while processing {@link API} queries.
 */
public class APIException extends Exception {
    /**
     * @param message A message to explain an exception.
     * @param cause   A cause occurred while processing.
     * @see Exception
     */
    public APIException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.giorgimode.dictionary.exception;

/**
 * Any exceptions related to dictionary.
 * 
 *
 */
public class DictionaryException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message the exception message
     * @param cause the cause
     */
    public DictionaryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the exception message
     */
    public DictionaryException(String message) {
        super(message);
    }

    /**
     * @param cause the cause
     */
    public DictionaryException(Throwable cause) {
        super(cause);
    }
}

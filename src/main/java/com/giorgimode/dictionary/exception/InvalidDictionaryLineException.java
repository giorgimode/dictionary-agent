package com.giorgimode.dictionary.exception;

/**
 * An exception for an invalid dictionary format.
 */
@SuppressWarnings("unused")
public class InvalidDictionaryLineException extends DictionaryException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message the exception message
     * @param cause   the cause
     */
    public InvalidDictionaryLineException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the exception message
     */
    public InvalidDictionaryLineException(String message) {
        super(message);
    }

    /**
     * @param cause the cause
     */
    public InvalidDictionaryLineException(Throwable cause) {
        super(cause);
    }
}

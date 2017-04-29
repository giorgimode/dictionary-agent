package com.giorgimode.dictionary.exception;

/**
 * An exception while reading a dictionary file.
 */
@SuppressWarnings("unused")
public class DictionaryReaderException extends DictionaryException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message the exception message
     * @param cause   the cause
     */
    public DictionaryReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the exception message
     */
    public DictionaryReaderException(String message) {
        super(message);
    }

    /**
     * @param cause the cause
     */
    public DictionaryReaderException(Throwable cause) {
        super(cause);
    }
}

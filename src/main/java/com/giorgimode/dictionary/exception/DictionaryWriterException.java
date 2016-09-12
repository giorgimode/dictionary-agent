package com.giorgimode.dictionary.exception;

/**
 * An exception while writing a dictionary file.
 */
public class DictionaryWriterException extends DictionaryException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message the exception message
     * @param cause   the cause
     */
    public DictionaryWriterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the exception message
     */
    public DictionaryWriterException(String message) {
        super(message);
    }

    /**
     * @param cause the cause
     */
    public DictionaryWriterException(Throwable cause) {
        super(cause);
    }
}

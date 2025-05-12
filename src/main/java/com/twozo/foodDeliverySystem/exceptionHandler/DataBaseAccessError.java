package com.twozo.foodDeliverySystem.exceptionHandler;

/**
 * Custom exception class to handle database access errors
 * Extends Exception and implements ExceptionHandler interface
 */
public class DataBaseAccessError extends Exception implements ExceptionHandler {

    /**
     * Constructor to create database access error with custom message
     *
     * @param message Error message describing the database access error
     */
    public DataBaseAccessError(final String message) {
        super(message);
    }

}
package com.batchExample.Movies.exception;

public class ErrorMessage {

    public static String failedCustomValidationMessage(String field) {
        return new StringBuilder()
                .append("Did not meet validation due to: ")
                .append(field).append(" is an invalid value")
                .toString();
    }
}

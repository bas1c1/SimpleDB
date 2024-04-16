package org.sbd;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Error {
    //protected static Logger logger = Logger.getLogger(String.valueOf(Error.class));

    private String errorMessage = "NO_ERROR";
    private boolean isError = false;
    private Exception exception = new Exception("NO_ERROR");

    public Error() { }

    public Error(boolean isError) {
        this.isError = isError;
    }

    public Error(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Error(Exception exception) {
        this.exception = exception;
    }

    public void executeException() {
        StringBuilder errorMessageBuilder = new StringBuilder();
        errorMessageBuilder.append(String.format("Cause of an exception %s terminating with code %d. Stack trace:\n", exception.getClass().getName(), exception.hashCode()));

        //TODO: logging errors
        exception.printStackTrace();
        System.out.println(errorMessageBuilder.toString());

        System.exit(exception.hashCode());
    }

    public void initException() {
        this.isError = true;
        this.errorMessage = this.exception.getMessage();
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

package com.plexpt.chatgpt.util.fastjson.exception;

import java.io.PrintStream;
import java.io.PrintWriter;


public class ParseException extends RuntimeException {
    private Exception processingException;


    public ParseException(Exception processingException) {
        this.processingException = processingException;
    }


    public ParseException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        processingException.printStackTrace();
    }

    @Override
    public String toString() {
        return processingException.toString();
    }

    @Override
    public String getMessage() {
        return processingException.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return processingException.getLocalizedMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return processingException.getCause();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        return processingException.initCause(cause);
    }

    @Override
    public void printStackTrace(PrintStream s) {
        processingException.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        processingException.printStackTrace(s);
    }
}

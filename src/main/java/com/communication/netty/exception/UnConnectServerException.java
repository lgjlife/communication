package com.communication.netty.exception;

public class UnConnectServerException extends Exception{

    public UnConnectServerException() {
    }

    public UnConnectServerException(String message) {
        super(message);
    }

    public UnConnectServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

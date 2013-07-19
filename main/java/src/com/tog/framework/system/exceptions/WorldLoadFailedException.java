package com.tog.framework.system.exceptions;

public class WorldLoadFailedException extends Exception {

    public WorldLoadFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorldLoadFailedException(String s) {
        super(s);
    }
}

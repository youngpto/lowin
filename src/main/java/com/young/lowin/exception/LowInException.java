package com.young.lowin.exception;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-08-26
 * Time: 4:29
 */
public class LowInException extends Exception{
    public LowInException() {
        super();
    }

    public LowInException(String message) {
        super(message);
    }

    public LowInException(String message, Throwable cause) {
        super(message, cause);
    }

    public LowInException(Throwable cause) {
        super(cause);
    }

    protected LowInException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

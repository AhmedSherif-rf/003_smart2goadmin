package com.ntg.sadmin.exceptions;

public class NTGRestException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String ErrorCode = "000";

    public String ErrorMessage;

    public String ErrorTrace;

    public String ErrorID;

    public NTGRestException() {

    }

    public NTGRestException(String ErrorCode, String ErrorMessage) {
        this.ErrorCode = ErrorCode;
        this.ErrorMessage = ErrorMessage;
    }


}

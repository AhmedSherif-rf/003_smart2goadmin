/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntg.sadmin.common;

import com.ntg.sadmin.exceptions.NTGRestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author IBOSS
 */
public class NTGMessageOperation {

    private static final Logger logger = LoggerFactory.getLogger(NTGMessageOperation.class);

    public static Exception lastPrintedException;

    static long ErrorNo = 0;

    public static void PrintErrorTrace(Exception err) {
        if (lastPrintedException == null || !lastPrintedException.equals(err)) {
            lastPrintedException = err;
            String e = GetErrorTrace(err);
            logger.error(e);
        }
    }

    public static String GetErrorTrace(Exception err) {
        long ErrNo = ErrorNo++;
         String M =(err instanceof NTGRestException)?((NTGRestException)err).ErrorMessage : err.getMessage();

        StringBuilder er = new StringBuilder("**************Error#" + ErrNo + " **********************\n");
        Throwable cerr = null;

        if (M == null) {
            M = "UnKnow Error";
        }

        if (err instanceof org.springframework.web.client.HttpServerErrorException) {
            M = ((org.springframework.web.client.HttpServerErrorException) err).getResponseBodyAsString();
        }
        er.append("Err Msg : " + M + "(" + err.getClass().getName() + ")");

        Throwable rootEx = err.getCause();
        while (rootEx != null) {
            if (rootEx.getMessage() != null) {
                er.append("\n");
                er.append(" Caused By Message (" + rootEx.getStackTrace()[0].getClassName() + ") : ");
                er.append(rootEx.getMessage());
            }
            rootEx = rootEx.getCause();
        }

        er.append("\n StageCRM ErrTrace: ..\n");

        java.lang.StackTraceElement[] ErrList = (cerr == null) ? err.getStackTrace() : cerr.getStackTrace();
        for (int i = 0; i < ErrList.length; i++) {
            String e = ErrList[i].toString();
            if (e.indexOf("com.ntg") > -1) {
                er.append("\n");
                er.append(e);
            }
        }
        er.append("\n************** End Error#" + ErrNo + " **********************");

        return er.toString();
    }

    public static String GetCurrentTrace() {
        String er = "";
        boolean foundNTGPak = false;
        java.lang.StackTraceElement[] ErrList = Thread.currentThread().getStackTrace();
        for (int i = 2; i < ErrList.length; i++) {
            String e = ErrList[i].toString();
            if (e.indexOf("com.ntg") > -1) {
                er += "\n" + e;
                foundNTGPak = true;
            }
        }

        if (!foundNTGPak) {
            for (int i = 2; i < 5; i++) {
                String e = ErrList[i].toString();
                er += "\n" + e;
            }
        }
        return er;
    }

}

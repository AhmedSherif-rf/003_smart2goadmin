 package com.ntg.sadmin.web.response;

 import com.ntg.sadmin.constants.CommonConstants;
 import com.ntg.sadmin.exceptions.NTGRestException;
 import com.ntg.sadmin.utils.Utils;

 /**
  * BaseResponse Will be extend by any response to have common response fields
  *
  * @author mashour@ntgclarity.com
  */
 @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
 @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

 public class BaseResponse {

     private boolean enableDeveloperLogs = Boolean.parseBoolean(Utils.loadFromPropertyFile(CommonConstants.APPLICATION_PROPERTIES_FILE_NAME, "enable.developer.logs"));

     private String code;
     private String key;
     private String message;
     private String stackTrace;

     private NTGRestException restException;

     public BaseResponse() {
         super();
     }

     public BaseResponse(String code, String key, String message) {
         super();
         this.code = code;
         this.key = key;
         this.message = message;
     }

     public BaseResponse(String code, String key, String message, String stackTrace) {
         super();
         this.code = code;
         this.key = key;
         this.message = message;
         this.stackTrace = stackTrace;
     }

     public BaseResponse(String code, String key, String message, String stackTrace, NTGRestException restException) {
         super();
         this.code = code;
         this.key = key;
         this.message = message;
         this.stackTrace = stackTrace;
         this.restException = restException;
     }

     public String getCode() {
         return code;
     }

     public void setCode(String code) {
         this.code = code;
     }

     public String getKey() {
         return key;
     }

     public void setKey(String key) {
         this.key = key;
     }

     public String getMessage() {
         return message;
     }

     public void setMessage(String message) {
         this.message = message;
     }

     public String getStackTrace() {
         return (enableDeveloperLogs) ? stackTrace : null;
     }

     public void setStackTrace(String stackTrace) {
         this.stackTrace = stackTrace;
     }

     public NTGRestException getRestException() {
         return (enableDeveloperLogs) ? restException : null;
     }

     public void setRestException(NTGRestException restException) {
         this.restException = restException;
     }

 }

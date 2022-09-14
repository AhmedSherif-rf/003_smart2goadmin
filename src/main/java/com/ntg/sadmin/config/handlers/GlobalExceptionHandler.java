package com.ntg.sadmin.config.handlers;

import com.ntg.sadmin.constants.CodesAndKeys;
import com.ntg.sadmin.web.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.ntg.sadmin.common.NTGMessageOperation;
import com.ntg.sadmin.exceptions.BusinessException;
import com.ntg.sadmin.exceptions.NTGRestException;
import com.ntg.sadmin.utils.NTGExceptionUtils;
import com.ntg.sadmin.web.response.StringResponse;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @Autowired
    NTGExceptionUtils ntgExceptionUtils;

    @ExceptionHandler(value = HttpServerErrorException.class)
    @ResponseBody
    public ResponseEntity<?>  handleException(HttpServerErrorException ex) {
        StringResponse response = new StringResponse();
        response.setRestException(new NTGRestException("RMSR001", ex.getResponseBodyAsString()));
        NTGMessageOperation.PrintErrorTrace(ex);
       // return response;
        return new ResponseEntity<>(
                new BaseResponse(CodesAndKeys.BUSINESS_ERROR_CODE,
                        CodesAndKeys.BUSINESS_ERROR_KEY,
                        ex.getMessage(),
                        NTGMessageOperation.GetErrorTrace(ex)),
                HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleException(Exception ex) {
        StringResponse response = new StringResponse();
        response.setRestException(new NTGRestException("0000", ntgExceptionUtils.GetErrorTrace(ex)));
        NTGMessageOperation.PrintErrorTrace(ex);
        //return response;
        return new ResponseEntity<>(
                new BaseResponse(CodesAndKeys.BUSINESS_ERROR_CODE,
                        CodesAndKeys.BUSINESS_ERROR_KEY,
                        ex.getMessage(),
                        NTGMessageOperation.GetErrorTrace(ex)),
                HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        return new ResponseEntity<>(ex.getBaseResponse(), HttpStatus.BAD_REQUEST);
    }

}

package com.ntg.sadmin.exceptions;

import com.ntg.sadmin.common.NTGMessageOperation;
import com.ntg.sadmin.constants.CodesAndKeys;
import com.ntg.sadmin.web.response.BaseResponse;

/**
 * BusinessException Will be used in case of business failure
 *
 * @author mashour@ntgclarity.com
 */
//@Component
public class BusinessException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private BaseResponse baseResponse;

    public BusinessException(String errorMessage) {
        super(errorMessage);
    }

    public BusinessException(String errorMessage, Exception err) {
        super(errorMessage, err);
        this.baseResponse = new BaseResponse(CodesAndKeys.BUSINESS_ERROR_CODE,CodesAndKeys.BUSINESS_ERROR_KEY,errorMessage, NTGMessageOperation.GetErrorTrace(err));
    }

    public BusinessException(String code, String key, String message) {
        this.baseResponse = new BaseResponse(code, key, message, NTGMessageOperation.GetErrorTrace(this));
    }

    public BaseResponse getBaseResponse( ) {
         if(baseResponse !=null){
             return baseResponse;
         }else{
             return new BaseResponse("000","Errro",this.getMessage());
         }
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        this.baseResponse = baseResponse;
    }
}

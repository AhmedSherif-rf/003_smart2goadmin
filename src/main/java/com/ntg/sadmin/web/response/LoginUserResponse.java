package com.ntg.sadmin.web.response;

import javax.xml.bind.annotation.XmlElement;

import com.ntg.sadmin.web.dto.LoginUser;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class LoginUserResponse extends BaseResponse {
	@XmlElement
	public LoginUser loginUser;
}

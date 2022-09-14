package com.ntg.sadmin.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.ntg.sadmin.utils.Utils;

@Component
public class RestEndpointFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(RestEndpointFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {

		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);

		filterChain.doFilter(requestWrapper, responseWrapper);

		logRequestAndResponse(requestWrapper, responseWrapper);

		responseWrapper.copyBodyToResponse();
	}

	public void logRequestAndResponse(ContentCachingRequestWrapper requestWrapper,
			ContentCachingResponseWrapper responseWrapper) {
		logger.debug("start logRequestAndResponse function");

		// Start Request
		StringBuilder requestStringBuilder = new StringBuilder();
		Map<String, String> parameters = Utils.buildParametersMap(requestWrapper);

		requestStringBuilder.append("REQUEST ");
		requestStringBuilder.append("method=[").append(requestWrapper.getMethod()).append("] ");
		requestStringBuilder.append("path=[").append(requestWrapper.getRequestURI()).append("] ");
		requestStringBuilder.append("headers=[").append(Utils.buildRequestHeadersMap(requestWrapper)).append("] ");

		if (!parameters.isEmpty()) {
			requestStringBuilder.append("parameters=[").append(parameters).append("] ");
		}
		String requestBody = new String(requestWrapper.getContentAsByteArray());
		requestStringBuilder.append("body=[" + requestBody + "]");

		// Start Response
		StringBuilder responseStringBuilder = new StringBuilder();
		responseStringBuilder.append("RESPONSE ");
		responseStringBuilder.append("method=[").append(requestWrapper.getMethod()).append("] ");
		responseStringBuilder.append("path=[").append(requestWrapper.getRequestURI()).append("] ");
		responseStringBuilder.append("responseHeaders=[").append(Utils.buildResponseHeadersMap(responseWrapper))
				.append("] ");
		String responseBody = new String(responseWrapper.getContentAsByteArray());
		responseStringBuilder.append("responseBody=[").append(responseBody).append("] ");

		logger.debug("====================================");
		logger.debug(requestStringBuilder.toString());
		logger.debug("====================================");
		logger.debug(responseStringBuilder.toString());
		logger.debug("====================================");
		logger.debug("end logRequestAndResponse function");

	}

}
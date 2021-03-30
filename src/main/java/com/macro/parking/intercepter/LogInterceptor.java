package com.macro.parking.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LogInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//		throws Exception {
//		logger.debug("Request URL:: {} {}",request.getMethod(), request.getRequestURL().toString());
//		request.getParameterMap().forEach((key, value) -> {
//			logger.debug("params :: {} {}", key,value);
//		});
//		return true;
//	}
}
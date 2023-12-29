package com.mashreq.oa.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;

@Component
public class TokenService {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(TokenService.class);

	@Autowired
	public HttpSession session;
	
	public boolean validateToken(Map<String, String> headers) {		
		if(headers != null) {		
			String token=headers.get("token");
			String username=headers.get("username");
			logger.info("validateToken - token:"+token+", Username:"+username);
			logger.info("token:"+session.getAttribute(username));
			if(token.equals((String) session.getAttribute(username))){	
				return true;
			}
		}
		return false;
	}

}

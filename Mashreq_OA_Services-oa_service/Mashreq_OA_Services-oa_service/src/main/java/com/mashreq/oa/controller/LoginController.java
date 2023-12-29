package com.mashreq.oa.controller;

import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.dao.UserDao;
import com.mashreq.oa.entity.User;
import com.mashreq.oa.service.UserService;
import com.mashreq.oa.utils.JwtUtil;

@RestController
@CrossOrigin
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	public static final String USERNAME_PARAM = "un";
	private byte[] ltpaToken = new byte[2048];
	private byte[] errorMessage = new byte[255];
	@Autowired
	public UserService userService;
	@Autowired
	public HttpSession session;

	@Value("${ldap.url}")
	private String ldapURL;
	@Autowired
	public UserDao userDao;
	@PostMapping("/login")
	public HashMap<String, String> login(@RequestBody User user, HttpServletResponse response) {
		LOGGER.info("Login - start");
		HashMap<String, String> map = new HashMap<>();

		boolean isUser = false;
		Boolean isUserExist;

		if (!(user.getUsername() == null) && !(user.getPassword() == null)) {
			LOGGER.info("ldap URL :"+ldapURL);
			isUser = getLdapContext(user.getUsername() + "@mashreq.com", user.getPassword(),ldapURL);

			if (isUser) {
				isUserExist = userService.isUserExist(user.getUsername());
				if (isUserExist) {
					LOGGER.info("Token generated");
					String token = JwtUtil.generateToken(user.getUsername());
					session.setAttribute(user.getUsername(), token);
					map.put("message", token);
					map.put("status", "200");
					return map;
				} else {
					session.removeAttribute(user.getUsername());
					map.put("message", "You are not authorized to access this application");
					map.put("status", "401");
					return map;
				}
			} else {
				session.removeAttribute(user.getUsername());
				map.put("message", "Please enter valid username and password");
				map.put("status", "403");
				return map;
			}
		} else {
			session.removeAttribute(user.getUsername());
			map.put("message", "Please enter valid username and password");
			map.put("status", "403");
			return map;
		}

		
//		
//		boolean isUserExist = userService.isUserExist(user.getUsername());
//		if (isUserExist & user.getPassword() != null && !"".equals(user.getPassword().trim())) {
//			isUser = getLdapContext(user.getUsername() + "@mashreq.com", user.getPassword(), ldapURL);
//
//			if (isUser) {
//				LOGGER.info("Token generated");
//				String token = JwtUtil.generateToken(user.getUsername());
//				session.setAttribute(user.getUsername(), token);
//				//update user table with token
//				//userDao.updateUserToken(user.getUsername(), token);
//				map.put("message", token);
//				map.put("status", "200");
//				return map;
//			} else {
//			session.removeAttribute(user.getUsername());
//				//update user table with empty token
//				//userDao.updateUserToken(user.getUsername(), "");
//				map.put("message", "Please enter valid username and password");
//				map.put("status", "403");
//				return map;
//			}
//		} else {
//			session.removeAttribute(user.getUsername());
//			//update user table with empty token
//			//userDao.updateUserToken(user.getUsername(), "");
//			map.put("message", "You are not authorized to access this application");
//			map.put("status", "401");
//			return map;
//		}
	}

	public static void main(String args[]) {
		System.out.println("Start Login controller ");
//		System.out.println(getLdapContext("raghuram@mashreq.com", "Mashreq@1234$",
//				"ldap://mashreqdoz1dc.mashreqbank.corp.network:389"));
		/*
		 * try { new LoginController().generateToken(); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
	}

	private static boolean getLdapContext(String principal, String credentials, String url) {
		LdapContext ctx = null;
		try {
			Hashtable<String, String> env = new Hashtable();
			env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
			env.put("java.naming.security.authentication", "simple");
			env.put("java.naming.security.principal", principal);
			env.put("java.naming.security.credentials", credentials);
			env.put("java.naming.provider.url", url);
			ctx = new InitialLdapContext(env, null);
			System.out.println("Connection Successful.");
			return true;
		} catch (AuthenticationException ex) {
			System.out.println(ex.getMessage());
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}

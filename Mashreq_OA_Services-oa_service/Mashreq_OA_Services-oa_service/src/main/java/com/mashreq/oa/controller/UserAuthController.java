package com.mashreq.oa.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.utils.JwtUtil;

@RestController
@CrossOrigin
public class UserAuthController {

	@Autowired
	public HttpSession session;
	
	@GetMapping("/auth")
	public boolean authnticateUser(@RequestParam("token") String token,@RequestParam("username") String username) {
		if(token.equals((String) session.getValue(username)))
		return true;
		else
			return false;
	}
	
}

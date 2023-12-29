package com.mashreq.oa.dao;

import java.util.List;

public interface UserDao {
	
	public List<String>  getUser(String username);
	public void updateUserToken(String username,String token);
}

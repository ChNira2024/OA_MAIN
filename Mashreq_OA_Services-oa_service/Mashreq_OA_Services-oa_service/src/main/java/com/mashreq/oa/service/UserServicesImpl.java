package com.mashreq.oa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mashreq.oa.dao.UserDao;


@Service
public class UserServicesImpl implements UserService {

	@Autowired
	public UserDao userDao;

	@Override
	public Boolean isUserExist(String username) {
		// TODO Auto-generated method stub
		if (userDao.getUser(username).size() > 0)
			return true;
		else
			return false;
	}


}

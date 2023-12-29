package com.mashreq.oa.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
	

	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<String> getUser(String username) {
		// TODO Auto-generated method stub
		try {
			String query="SELECT USERNAME FROM "+DBAPPEND+"OA_USER WHERE UPPER(USERNAME)=UPPER('"+username+"') AND ACTIVE='Y'";
			List<String> data=jdbcTemplate.queryForList(query,String.class);
			return data;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	@Override
	public void updateUserToken(String username, String token) {
		// TODO Auto-generated method stub
				try {
					String query="UPDATE  "+DBAPPEND+"OA_USER SET TOKEN=? WHERE UPPER(USERNAME)=UPPER('"+username+"') AND ACTIVE='Y'";
					Object[] args = {token};
					jdbcTemplate.update(query, args);
					
				}
				catch(Exception e) {
					e.printStackTrace();
				
				}
		
	}
}

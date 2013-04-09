package com.ineatconseil.yougo.dao;

import com.ineatconseil.yougo.bo.User;

public interface UserDao extends GenericDao<User, Long>{
	
	/**
	 * Finds a user using its email
	 * 
	 * @param email
	 * @return The user or null if login couldn't be found 
	 */
	User findByEmail(String email);
	
}

package com.ineatconseil.yougo.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ineatconseil.yougo.bo.User;
import com.ineatconseil.yougo.dao.UserDao;

/**
 * This class is a concrete class which implements {@link org.springframework.security.core.userdetails.UserDetailsService}
 * to provide authentication mechanisms through Spring Security.
 * 
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see org.springframework.security.core.userdetails.UserDetails
 */
@Service(value = "userDetailsService")
public class UserDetailsService implements
		org.springframework.security.core.userdetails.UserDetailsService {

	/**
	 * Logger
	 */
	private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	private UserDao userDao;

	/**
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		log.debug("A user tries to log in with username [{}]", username);

		User user = userDao.findByEmail(username);

		if (user == null) {
			log.debug("User [{}] doesn't exist.", username);
			throw new UsernameNotFoundException("User [{}] doesn't exist." + username);
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new GrantedAuthorityImpl("USER"));

		if (user.getAdmin())
			authorities.add(new GrantedAuthorityImpl("ADMIN"));

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getPassword(), user.getActive(), 
				true, true, true, authorities);
	}

}

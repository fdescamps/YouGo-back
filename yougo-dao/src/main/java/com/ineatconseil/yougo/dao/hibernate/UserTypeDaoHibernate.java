package com.ineatconseil.yougo.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ineatconseil.yougo.bo.UserType;
import com.ineatconseil.yougo.dao.UserTypeDao;

@Repository("userTypeDao")
@Transactional
public class UserTypeDaoHibernate extends GenericDaoHibernate<UserType, Long> implements UserTypeDao {

	public UserTypeDaoHibernate() {
		super(UserType.class);
	}
	
}

package com.ineatconseil.yougo.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ineatconseil.yougo.bo.User;
import com.ineatconseil.yougo.dao.UserDao;

@Repository("userDao")
@Transactional
public class UserDaoHibernate extends GenericDaoHibernate<User, Long> implements UserDao {

	public UserDaoHibernate() {
		super(User.class);
	}

	public User findByEmail(String email) {
		log.debug("Find a user by his email [{}]", email);
		
		Criteria criteria = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("email", email));
		
		User user = (User) criteria.uniqueResult();

		if (user == null) {
			log.warn("User object with email '" + email + "' not found...");
			throw new ObjectRetrievalFailureException(User.class, email);
		}
		
		return user;
	}

}

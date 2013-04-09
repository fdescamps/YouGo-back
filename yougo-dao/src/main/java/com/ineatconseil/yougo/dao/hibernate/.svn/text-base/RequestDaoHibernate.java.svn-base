package com.ineatconseil.yougo.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ineatconseil.yougo.bo.Request;
import com.ineatconseil.yougo.bo.RequestStatus;
import com.ineatconseil.yougo.dao.RequestDao;

@Repository("requestDao")
@Transactional
public class RequestDaoHibernate extends GenericDaoHibernate<Request, Long>
		implements RequestDao {

	public RequestDaoHibernate() {
		super(Request.class);
	}

	@SuppressWarnings("unchecked")
	public List<Request> findByCriteria(Long userId, Date from, Date to,
			List<RequestStatus> requestStatus, Boolean fetchUser) {
		log.debug("Find requests by criteria [{}, {}, {}]", new Object [] {from, to, requestStatus});
		
		Criteria criteria = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createCriteria(Request.class);

		if (from != null && to == null)
			criteria.add(Restrictions.ge("to", from));
		
		else if (from == null && to != null)
			criteria.add(Restrictions.le("from", to));
		
		else if (from != null && to != null)
			criteria.add(Restrictions.and(Restrictions.ge("to", from), Restrictions.le("from", to)));
		
		if (requestStatus != null && !requestStatus.isEmpty())
			criteria.add(Restrictions.in("status", requestStatus));

		if (userId != null)
			criteria.add(Restrictions.eq("user.id", userId));
		
		if (fetchUser)
			criteria.setFetchMode("user", FetchMode.JOIN);

		return criteria.list();
	}

}

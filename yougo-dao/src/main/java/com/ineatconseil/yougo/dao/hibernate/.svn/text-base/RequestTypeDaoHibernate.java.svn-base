package com.ineatconseil.yougo.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ineatconseil.yougo.bo.RequestType;
import com.ineatconseil.yougo.dao.RequestTypeDao;

@Repository("requestTypeDao")
@Transactional
public class RequestTypeDaoHibernate extends GenericDaoHibernate<RequestType, Long> implements RequestTypeDao {

	public RequestTypeDaoHibernate() {
		super(RequestType.class);
	}

}

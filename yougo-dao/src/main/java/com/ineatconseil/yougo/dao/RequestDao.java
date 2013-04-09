package com.ineatconseil.yougo.dao;

import java.util.Date;
import java.util.List;

import com.ineatconseil.yougo.bo.Request;
import com.ineatconseil.yougo.bo.RequestStatus;

public interface RequestDao extends GenericDao<Request, Long> {

	public List<Request> findByCriteria(Long userId, Date from, Date to, List<RequestStatus> requestStatus, Boolean fetchUser);

}

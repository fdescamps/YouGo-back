package com.ineatconseil.yougo.ws.rs.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ineatconseil.yougo.bo.Request;
import com.ineatconseil.yougo.bo.RequestStatus;
import com.ineatconseil.yougo.dao.RequestDao;
import com.ineatconseil.yougo.dto.DetailedUserDto;
import com.ineatconseil.yougo.dto.RequestDto;
import com.ineatconseil.yougo.dto.RequestStatusDto;
import com.ineatconseil.yougo.security.Roles;
import com.ineatconseil.yougo.ws.rs.api.DetailedUserRestService;
import com.ineatconseil.yougo.ws.rs.impl.annotation.RestService;

@Path("/detailed-users")
@RestService
public class DetailedUserRestServiceImpl implements DetailedUserRestService {

	/**
	 * Logger SLF4J
	 */
	private final Logger log = LoggerFactory.getLogger(DetailedUserRestServiceImpl.class);
	
	@Autowired
	private RequestDao requestDao;

	@Autowired
	private Mapper dozerMapper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.ADMIN_ROLE)
	public List<DetailedUserDto> find(@QueryParam("from") Date from, @QueryParam("to") Date to,
			@QueryParam("requestStatus") List<RequestStatusDto> requestStatusDtos) {
		log.debug("Find users with their requests using request criteria : from {} to {} with {} status", new Object[]{from, to, requestStatusDtos});
	
		// Mapping requestStatusDtos list to requestStatus list
		List<RequestStatus> requestStatus = new ArrayList<RequestStatus>(requestStatusDtos.size());
		for (RequestStatusDto requestStatusDto : requestStatusDtos)
			requestStatus.add(RequestStatus.valueOf(requestStatusDto.name()));
		
		List<Request> requests = requestDao.findByCriteria(null, from, to, requestStatus, true);
		return new ArrayList<DetailedUserDto>(reverseAndMap(requests));
	}
	
	/**
	 * Reverse Request/User many-to-one association to a User/Request one-to-many and map them to a DetailedUserDto/RequestDto
	 * 
	 * @param requests List of Request with users
	 * @return List of DetailedUserdDto with requests
	 */
	private Collection<DetailedUserDto> reverseAndMap(List<Request> requests) {
		Map<Long, DetailedUserDto> detailedUserDtosMap = new HashMap<Long, DetailedUserDto>();
		
		for (Request request : requests) {
			if (!detailedUserDtosMap.containsKey(request.getUser().getId())) {
				DetailedUserDto detailedUserDto = dozerMapper.map(request.getUser(), DetailedUserDto.class);
				detailedUserDto.setRequests(new TreeSet<RequestDto>());
				detailedUserDtosMap.put(request.getUser().getId(), detailedUserDto);
			}
			
			RequestDto requestDto = dozerMapper.map(request, RequestDto.class);
			detailedUserDtosMap.get(request.getUser().getId()).getRequests().add(requestDto);
		}
		
		return new TreeSet<DetailedUserDto>(detailedUserDtosMap.values());
	}

}

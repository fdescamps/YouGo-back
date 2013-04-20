package com.ineatconseil.yougo.ws.rs.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ineatconseil.yougo.bo.Request;
import com.ineatconseil.yougo.bo.RequestStatus;
import com.ineatconseil.yougo.bo.RequestType;
import com.ineatconseil.yougo.bo.User;
import com.ineatconseil.yougo.dao.RequestDao;
import com.ineatconseil.yougo.dao.RequestTypeDao;
import com.ineatconseil.yougo.dao.UserDao;
import com.ineatconseil.yougo.dto.RequestDto;
import com.ineatconseil.yougo.dto.RequestStatusDto;
import com.ineatconseil.yougo.security.Roles;
import com.ineatconseil.yougo.ws.rs.api.RequestRestService;
import com.ineatconseil.yougo.ws.rs.impl.annotation.RestService;

@Path("/users/{userId}/requests")
@RestService
public class RequestRestServiceImpl implements RequestRestService {

	/**
	 * Logger SLF4J
	 */
	private final Logger log = LoggerFactory.getLogger(RequestRestServiceImpl.class);

	@Autowired
	private RequestDao requestDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RequestTypeDao requestTypeDao;

	@Autowired
	private Mapper dozerMapper;
	
	@Context
	private UriInfo uriInfo;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ Roles.USER_ROLE, Roles.ADMIN_ROLE })
	public RequestDto find(@Context SecurityContext securityContext, @PathParam("userId") Long userId, @PathParam("id") Long id) {
		log.debug("Find request for user_id {} and request_id {}", userId, id);

		// If user is not admin he can't retrieve other user's request
		if (!securityContext.isUserInRole(Roles.ADMIN_ROLE)) {
			String userMail = securityContext.getUserPrincipal().getName();
			User user = userDao.findByEmail(userMail);
			if (!user.getId().equals(userId))
				throw new WebApplicationException(Status.FORBIDDEN);
		}
		
		Request request = requestDao.get(id);

		if (!request.getUser().getId().equals(userId))
			throw new WebApplicationException(Status.BAD_REQUEST);

		return dozerMapper.map(request, RequestDto.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ Roles.USER_ROLE, Roles.ADMIN_ROLE })
	public List<RequestDto> find(
			@Context SecurityContext securityContext,
			@PathParam("userId") Long userId,
			@QueryParam("from") Date from,
			@QueryParam("to") Date to,
			@QueryParam("requestStatus") List<RequestStatusDto> requestStatusDtos) {
		log.debug("Find requests using request criteria : user {} from {} to {} with {} status", new Object[]{userId, from, to, requestStatusDtos});

		// If user is not admin he can't retrieve other user's requests
		if (!securityContext.isUserInRole(Roles.ADMIN_ROLE)) {
			String userMail = securityContext.getUserPrincipal().getName();
			User user = userDao.findByEmail(userMail);
			if (!user.getId().equals(userId))
				throw new WebApplicationException(Status.FORBIDDEN);
		}
		
		if (from == null && to == null && requestStatusDtos.isEmpty())
			return findAll(userId);

		else if (from != null || to != null || !requestStatusDtos.isEmpty())
			return findByCriteria(userId, from, to, requestStatusDtos);

		else
			throw new WebApplicationException(Status.BAD_REQUEST);
	}

	private List<RequestDto> findAll(Long userId) {
		log.debug("Find all requests for user_id {}", userId);
		
		Set<Request> requests = userDao.get(userId).getRequests();
		
		List<RequestDto> requestDtos = new ArrayList<RequestDto>(requests.size());
		for (Request request : requests)
			requestDtos.add(dozerMapper.map(request, RequestDto.class));
		
		Collections.sort(requestDtos);

		return requestDtos;
	}

	private List<RequestDto> findByCriteria(Long userId, Date from, Date to,
			List<RequestStatusDto> requestStatusDtos) {
		log.debug("Find all requests for user_id {} from {} to {} with {} status", new Object[] {userId, from, to, requestStatusDtos});

		// Mapping requestStatusDtos list to requestStatus list 
		List<RequestStatus> requestStatus = new ArrayList<RequestStatus>(requestStatusDtos.size());
		for (RequestStatusDto requestStatusDto : requestStatusDtos)
			requestStatus.add(RequestStatus.valueOf(requestStatusDto.name()));

		List<Request> requests = requestDao.findByCriteria(userId, from, to, requestStatus, false);
		
		List<RequestDto> requestDtos = new ArrayList<RequestDto>(requests.size());
		for (Request request : requests)
			requestDtos.add(dozerMapper.map(request, RequestDto.class));

		Collections.sort(requestDtos);
		
		return requestDtos;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.USER_ROLE)
	public Response save(@PathParam("userId") Long userId, RequestDto requestDto) {
		log.debug("Save request for user_id {} and request {}", userId, requestDto);

		if (requestDto.getId() != null)
			throw new WebApplicationException(Status.BAD_REQUEST);

		// Is new request overlapping another one from same user
		if (requestDao.findByCriteria(userId, requestDto.getFrom(), requestDto.getTo(), null, false).size() > 0)
			throw new WebApplicationException(Status.BAD_REQUEST);
		
		Request request = dozerMapper.map(requestDto, Request.class);
		request.setUser(userDao.get(userId));
		request = requestDao.save(request);

		// Building URI
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		URI uri = uriBuilder.path(request.getId().toString()).build();

		return Response.created(uri).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ Roles.USER_ROLE, Roles.ADMIN_ROLE })
	public Response update(
			@Context SecurityContext securityContext,
			@PathParam("userId") Long userId,
			@PathParam("id") Long id,
			RequestDto requestDto) {
		log.debug("Update request for id {} and request {}", id, requestDto);

		if (requestDto.getId() == null || (requestDto.getId() != null && !requestDto.getId().equals(id)))
			throw new WebApplicationException(Status.BAD_REQUEST);

		// Is request overlapping another one from same user
		List<Request> requests = requestDao.findByCriteria(userId, requestDto.getFrom(), requestDto.getTo(), null, false);
		if (requests.size() > 1 || requests.size() == 1 && !(requests.get(0).getId().equals(id)))
			throw new WebApplicationException(Status.BAD_REQUEST);

		Request request = requestDao.get(id);
		if (!request.getUser().getId().equals(userId))
			throw new WebApplicationException(Status.BAD_REQUEST);

		// If user is not admin
		if (!securityContext.isUserInRole(Roles.ADMIN_ROLE)) {
			String userMail = securityContext.getUserPrincipal().getName();
			User user = userDao.findByEmail(userMail);

			// Does he own the request?
			if (!user.getId().equals(request.getUser().getId()))
				return Response.status(Status.FORBIDDEN).build();
			
			// Is he trying to accept/refuse his own request?
			if (requestDto.getStatus().equals(RequestStatusDto.ACCEPTED) || requestDto.getStatus().equals(RequestStatusDto.REFUSED))
				return Response.status(Status.FORBIDDEN).build();
			
			// Is he trying to update an already accepted/refused request?
			if (request.getStatus().equals(RequestStatusDto.ACCEPTED) || request.getStatus().equals(RequestStatusDto.REFUSED))
				return Response.status(Status.FORBIDDEN).build();
		}

		// If type has changed
		RequestType requestType = request.getType();
		if (!requestDto.getTypeId().equals(requestType.getId()))
			requestType = requestTypeDao.get(requestDto.getTypeId());
		
		request.setType(requestType);
		dozerMapper.map(requestDto, request);
		requestDao.save(request);
		
		return Response.ok().build();
	}

	@DELETE
	@Path("/{id}")
	@RolesAllowed(Roles.ADMIN_ROLE)
	public Response delete(@PathParam("id") Long id) {
		log.debug("Delete request for id {}", id);

		requestDao.remove(id);
		return Response.ok().build();
	}
	
}

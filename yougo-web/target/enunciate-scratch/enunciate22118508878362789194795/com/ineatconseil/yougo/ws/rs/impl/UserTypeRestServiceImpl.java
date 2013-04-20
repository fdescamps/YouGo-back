package com.ineatconseil.yougo.ws.rs.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ineatconseil.yougo.bo.UserType;
import com.ineatconseil.yougo.dao.UserTypeDao;
import com.ineatconseil.yougo.dto.UserTypeDto;
import com.ineatconseil.yougo.security.Roles;
import com.ineatconseil.yougo.ws.rs.api.UserTypeRestService;
import com.ineatconseil.yougo.ws.rs.impl.annotation.RestService;

/**
 * This class is a concrete implementation of the {@link UserTypeRestService}
 * interface RESTful services to manage {@link UserTypeDto} object type.
 * 
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 * @see com.ineatconseil.yougo.ws.rs.api.UserTypeRestService
 * @see com.ineatconseil.yougo.dto.UserTypeDto
 */
@Path("/user-types")
@RestService
public class UserTypeRestServiceImpl implements UserTypeRestService {

	/**
	 * Logger SLF4J
	 */
	private final Logger log = LoggerFactory.getLogger(UserTypeRestServiceImpl.class);

	@Autowired
	private UserTypeDao dao;

	@Autowired
	private Mapper dozerMapper;
	
	@Context
	private UriInfo uriInfo;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public UserTypeDto find(@PathParam("id") Long id) {
		log.debug("Get user type for id {}", id);

		return dozerMapper.map(dao.get(id), UserTypeDto.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserTypeDto> findAll() {
		log.debug("Get all user types");

		List<UserType> userTypes = dao.findAll();

		List<UserTypeDto> userTypeDtos = new ArrayList<UserTypeDto>(userTypes.size());
		for (UserType userType : userTypes)
			userTypeDtos.add(dozerMapper.map(userType, UserTypeDto.class));

		return userTypeDtos;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.ADMIN_ROLE)
	public Response save(UserTypeDto userTypeDto) {
		log.debug("Save user type {}", userTypeDto);

		if (userTypeDto.getId() != null)
			throw new WebApplicationException(Status.BAD_REQUEST);

		UserType userType = dao.save(dozerMapper.map(userTypeDto, UserType.class));

		// Building URI
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		URI uri = uriBuilder.path(userType.getId().toString()).build();

		return Response.created(uri).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.ADMIN_ROLE)
	public Response update(@PathParam("id") Long id, UserTypeDto userTypeDto) {
		log.debug("Update user type for id {} and user_type {}", id, userTypeDto);

		if (userTypeDto.getId() == null || userTypeDto.getId() != null && !userTypeDto.getId().equals(id))
			throw new WebApplicationException(Status.BAD_REQUEST);

		UserType userType = dao.get(id);
		dozerMapper.map(userTypeDto, userType);
		dao.save(userType);

		return Response.ok().build();
	}

	@DELETE
	@Path("/{id}")
	@RolesAllowed(Roles.ADMIN_ROLE)
	public Response delete(@PathParam("id") Long id) {
		log.debug("Delete user type for id {}", id);

		dao.remove(id);
		return Response.ok().build();
	}

}

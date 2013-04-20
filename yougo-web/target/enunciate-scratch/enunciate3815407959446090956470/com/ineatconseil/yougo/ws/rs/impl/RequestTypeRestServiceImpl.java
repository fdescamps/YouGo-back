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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ineatconseil.yougo.bo.RequestType;
import com.ineatconseil.yougo.dao.RequestTypeDao;
import com.ineatconseil.yougo.dto.RequestTypeDto;
import com.ineatconseil.yougo.security.Roles;
import com.ineatconseil.yougo.ws.rs.api.RequestTypeRestService;
import com.ineatconseil.yougo.ws.rs.impl.annotation.RestService;

@Path("/request-types")
@RestService
public class RequestTypeRestServiceImpl implements RequestTypeRestService {

	/**
	 * Logger SLF4J
	 */
	private final Logger log = LoggerFactory.getLogger(RequestTypeRestServiceImpl.class);

	@Autowired
	private RequestTypeDao dao;

	@Autowired
	private Mapper dozerMapper;

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public RequestTypeDto find(@PathParam("id") Long id) {
		log.debug("Get request type for id {}", id);

		return dozerMapper.map(dao.get(id), RequestTypeDto.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<RequestTypeDto> findAll() {
		log.debug("Get all request types");

		List<RequestType> requestTypes = dao.findAll();

		List<RequestTypeDto> requestTypeDtos = new ArrayList<RequestTypeDto>(requestTypes.size());
		for (RequestType requestType : requestTypes)
			requestTypeDtos.add(dozerMapper.map(requestType, RequestTypeDto.class));

		return requestTypeDtos;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.ADMIN_ROLE)
	public Response save(RequestTypeDto requestTypeDto) {
		log.debug("Save request type {}", requestTypeDto);

		if (requestTypeDto.getId() != null)
			throw new WebApplicationException(Status.BAD_REQUEST);

		RequestType requestType = dao.save(dozerMapper.map(requestTypeDto, RequestType.class));

		// Building URI
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		URI uri = uriBuilder.path(requestType.getId().toString()).build();

		return Response.created(uri).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.ADMIN_ROLE)
	public Response update(@PathParam("id") Long id, RequestTypeDto requestTypeDto) {
		log.debug("Update request type for id {} and request_type {}", id, requestTypeDto);

		if (requestTypeDto.getId() == null || requestTypeDto.getId() != null && !requestTypeDto.getId().equals(id))
			throw new WebApplicationException(Status.BAD_REQUEST);

		RequestType requestType = dao.get(id);
		dozerMapper.map(requestTypeDto, requestType);
		dao.save(requestType);
		
		return Response.ok().build();
	}

	@DELETE
	@Path("/{id}")
	@RolesAllowed(Roles.ADMIN_ROLE)
	public Response delete(@PathParam("id") Long id) {
		log.debug("Delete request type for id {}", id);
		
		dao.remove(id);
		return Response.ok().build();
	}

}

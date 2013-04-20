package com.ineatconseil.yougo.ws.rs.impl;

import com.ineatconseil.yougo.bo.User;
import com.ineatconseil.yougo.bo.UserType;
import com.ineatconseil.yougo.dao.UserDao;
import com.ineatconseil.yougo.dao.UserTypeDao;
import com.ineatconseil.yougo.dto.UserDto;
import com.ineatconseil.yougo.security.Roles;
import com.ineatconseil.yougo.ws.rs.api.UserRestService;
import com.ineatconseil.yougo.ws.rs.impl.annotation.RestService;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("/users")
@RestService
public class UserRestServiceImpl implements UserRestService {

	/**
	 * Logger SLF4J
	 */
	private final Logger log = LoggerFactory.getLogger(UserRestServiceImpl.class);

	@Autowired
	private UserDao dao;
	
	@Autowired
	private UserTypeDao userTypeDao;

	@Autowired
	private Mapper dozerMapper;
	
	@Context
	private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN_ROLE)
    public List<UserDto> findAll() {
        log.debug("Get all users");

        List<User> users = dao.findAll();

        List<UserDto> userDtos = new ArrayList<UserDto>(users.size());
        for (User user : users)
            userDtos.add(dozerMapper.map(user, UserDto.class));

        return userDtos;
    }

	@GET
	@Path("/{id:\\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.ADMIN_ROLE, Roles.USER_ROLE})
	public UserDto find(@Context SecurityContext securityContext, @PathParam("id") Long id) {
		log.debug("Get user for id {}", id);

		// If user is not admin he can't retrieve other user's requests
		if (!securityContext.isUserInRole(Roles.ADMIN_ROLE)) {
			String userMail = securityContext.getUserPrincipal().getName();
			User user = dao.findByEmail(userMail);
			if (!user.getId().equals(id))
				throw new WebApplicationException(Status.FORBIDDEN);
		}
		
		return dozerMapper.map(dao.get(id), UserDto.class);
	}

	@GET
	@Path("/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.ADMIN_ROLE, Roles.USER_ROLE})
	public UserDto findByEmail(@Context SecurityContext securityContext, @PathParam("email") String email) {
		log.debug("Get user for email {}", email);

		// If user is not admin he can't retrieve other user's requests
		if (!securityContext.isUserInRole(Roles.ADMIN_ROLE)) {
			String userMail = securityContext.getUserPrincipal().getName();
			if (!email.equals(userMail))
				throw new WebApplicationException(Status.FORBIDDEN);
		}
		
		return dozerMapper.map(dao.findByEmail(email), UserDto.class);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.ADMIN_ROLE)
	public Response save(UserDto userDto) {
		log.debug("Save user {}", userDto);

		if (userDto.getId() != null)
			throw new WebApplicationException(Status.BAD_REQUEST);

		// TODO Il faut aussi prendre en compte les violations de contrainte d'unicité retour 4xx ou 5xx
		User user = dozerMapper.map(userDto, User.class);

		// Assigning default password
		// TODO Put value in properties file
		user.setPassword("password");
		user = dao.save(user);

		// Building URI
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		URI uri = uriBuilder.path(user.getId().toString()).build();

		return Response.created(uri).build();
	}

	@PUT
	@Path("/{id:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.ADMIN_ROLE)
	public Response update(@Context SecurityContext securityContext, @PathParam("id") Long id, UserDto userDto) {
		log.debug("Update user for id {} and user {}", id, userDto);

		if (userDto.getId() == null || userDto.getId() != null && !userDto.getId().equals(id))
			throw new WebApplicationException(Status.BAD_REQUEST);

		User user = dao.get(id);
		
		// Is admin trying to remove admin or activation from his own account?
		String userMail = securityContext.getUserPrincipal().getName();
		if (user.getEmail().equals(userMail) && (!userDto.getActive() || !userDto.getAdmin()))
			return Response.status(Status.FORBIDDEN).build();

		// If type has changed
		UserType userType = user.getType();
		if (!userDto.getTypeId().equals(userType.getId()))
			userType = userTypeDao.get(userDto.getTypeId());

		// TODO Il faut aussi prendre en compte les violations de contrainte d'unicité retour 4xx ou 5xx
		user.setType(userType);
		dozerMapper.map(userDto, user);
		dao.save(user);

		return Response.ok().build();
	}

	@DELETE
	@Path("/{id:\\d+}")
	@RolesAllowed(Roles.ADMIN_ROLE)
	public Response delete(@Context SecurityContext securityContext, @PathParam("id") Long id) {
		log.debug("Delete user for id {}", id);

		// Is admin trying to delete himself?
		String userMail = securityContext.getUserPrincipal().getName();
		User user = dao.findByEmail(userMail);
		if (user.getId().equals(id))
			return Response.status(Status.FORBIDDEN).build();
		
		dao.remove(id);
		return Response.ok().build();
	}

	@PUT
	@Path("/{id}/password")
	@RolesAllowed(Roles.USER_ROLE)
	public Response updatePassword(
			@Context SecurityContext securityContext,
			@PathParam("id") Long id,
			@FormParam("oldPassword") String oldPassword,
			@FormParam("newPassword") String newPassword,
			@FormParam("confirmedNewPassword") String confirmedNewPassword) {
		log.debug("Update password for user_id {} ", id);

		if (!newPassword.equals(confirmedNewPassword))
			throw new WebApplicationException(Status.BAD_REQUEST);
		
		String userMail = securityContext.getUserPrincipal().getName();
		User user = dao.findByEmail(userMail);

		if (!user.getId().equals(id) || !user.getPassword().equals(oldPassword))
			throw new WebApplicationException(Status.BAD_REQUEST);

		user.setPassword(newPassword);
		dao.save(user);

		return Response.ok().build();
	}

}

package com.ineatconseil.yougo.rest;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.ineatconseil.yougo.dto.UserTypeDto;
import com.ineatconseil.yougo.util.AbstractWebRunnerTestCase;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

public class UserTypeITest extends AbstractWebRunnerTestCase {
	
	public WebResource getWebResource() {
		return this.getResource("user-types");
	}
	
	@Test
	public void findById() {
		WebResource wr = getWebResource().path("1");
		
		// Validation du code retour
		ClientResponse cr = wr.get(ClientResponse.class);
		assertEquals(Status.OK.getStatusCode(), cr.getStatus());
		
		// Validation de l'entité contenue dans la réponse
		UserTypeDto userType = wr.accept(MediaType.APPLICATION_JSON_TYPE).get(UserTypeDto.class);
		assertNotNull(userType);
		assertTrue(userType.getId() == 1L);
	}
	
	@Test
	public void findAll() {
		// Validation du code retour
		ClientResponse cr = getWebResource().get(ClientResponse.class);
		assertEquals(Status.OK.getStatusCode(), cr.getStatus());
		
		// Validation de l'entité contenue dans la réponse
		List<UserTypeDto> lstUserType = getWebResource().accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<UserTypeDto>>() {});
		assertNotNull(lstUserType);
		assertTrue(lstUserType.size() > 0);
	}
	
	@Test
	public void save() {
		UserTypeDto newUserType = new UserTypeDto();
		newUserType.setDescription("User type description");
		
		// Validation du code retour
		ClientResponse cr = getWebResource().type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, newUserType);
		assertNotNull(cr);
		assertEquals(cr.getClientResponseStatus(), Status.CREATED);
		
		// Validation de l'identifiant de la nouvelle ressource
		String locationURI = cr.getLocation().getPath();
		Long createdUserTypeId = Long.parseLong(locationURI.substring(locationURI.lastIndexOf("/") + 1));
		assertTrue(createdUserTypeId > 0);
	}
	
	@Test
	public void update() {
		UserTypeDto updateUserType = new UserTypeDto();
		updateUserType.setId(1L);
		updateUserType.setDescription("New user type description");
		
		// Validation du code retour
		ClientResponse cr = getWebResource().path("1").type(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class, updateUserType);
		assertNotNull(cr);
		assertEquals(cr.getClientResponseStatus(), Status.OK);
	}
}

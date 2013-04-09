package com.ineatconseil.yougo.rest;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.ineatconseil.yougo.dto.UserDto;
import com.ineatconseil.yougo.util.AbstractWebRunnerTestCase;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;

public class UserITest extends AbstractWebRunnerTestCase {
	
	public WebResource getWebResource() {
		return this.getResource("users");
	}
	
	@Test
	public void findById() {
		WebResource wr = getWebResource().path("1");
		
		// Validation du code retour
		ClientResponse cr = wr.get(ClientResponse.class);
		assertEquals(Status.OK.getStatusCode(), cr.getStatus());
		
		// Validation de l'entité contenue dans la réponse
		UserDto user = wr.accept(MediaType.APPLICATION_JSON_TYPE).get(UserDto.class);
		assertNotNull(user);
		assertTrue(user.getId() == 1L);
	}
	
	@Test
	public void findByEmail() {
		String mail = "kristina.chung@company.com";
		WebResource wr = getWebResource().path(mail);
		
		// Validation du code retour
		ClientResponse cr = wr.get(ClientResponse.class);
		assertEquals(Status.OK.getStatusCode(), cr.getStatus());
		
		// Validation de l'entité contenue dans la réponse
		UserDto user = wr.accept(MediaType.APPLICATION_JSON_TYPE).get(UserDto.class);
		assertNotNull(user);
		assertEquals(user.getEmail(), mail);
	}
	
	@Test
	public void findAll() {
		// Validation du code retour
		ClientResponse cr = getWebResource().get(ClientResponse.class);
		assertEquals(Status.OK.getStatusCode(), cr.getStatus());
		
		// Validation de l'entité contenu dans la réponse
		List<UserDto> lstUser = getWebResource().accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<UserDto>>() {});
		assertNotNull(lstUser);
		assertTrue(lstUser.size() > 0);
	}
}

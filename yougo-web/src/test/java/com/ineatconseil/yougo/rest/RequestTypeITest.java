package com.ineatconseil.yougo.rest;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.ineatconseil.yougo.dto.RequestTypeDto;
import com.ineatconseil.yougo.util.AbstractWebRunnerTestCase;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

public class RequestTypeITest extends AbstractWebRunnerTestCase {
	
	public WebResource getWebResource() {
		return this.getResource("request-types");
	}
	
	@Test
	public void findById() {
		WebResource wr = getWebResource().path("1");
		
		// Validation du code retour
		ClientResponse cr = wr.get(ClientResponse.class);
		assertEquals(Status.OK.getStatusCode(), cr.getStatus());
		
		// Validation de l'entité contenue dans la réponse
		RequestTypeDto requestType = wr.accept(MediaType.APPLICATION_JSON_TYPE).get(RequestTypeDto.class);
		assertNotNull(requestType);
		assertTrue(requestType.getId() == 1L);
	}
	
	@Test
	public void findAll() {
		// Validation du code retour
		ClientResponse cr = getWebResource().get(ClientResponse.class);
		assertEquals(Status.OK.getStatusCode(), cr.getStatus());
		
		// Validation de l'entité contenu dans la réponse
		List<RequestTypeDto> lstRequestType = getWebResource().accept(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<RequestTypeDto>>() {});
		assertNotNull(lstRequestType);
		assertTrue(lstRequestType.size() > 0);
	}
	
	@Test
	public void save() {
		RequestTypeDto newRequestType = new RequestTypeDto();
		newRequestType.setDescription("Request type description");
		
		// Validation du code retour
		ClientResponse cr = getWebResource().type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, newRequestType);
		assertNotNull(cr);
		assertEquals(cr.getClientResponseStatus(), Status.CREATED);
		
		// Validation de l'identifiant de la nouvelle ressource
		String locationURI = cr.getLocation().getPath();
		Long createdRequestTypeId = Long.parseLong(locationURI.substring(locationURI.lastIndexOf("/") + 1));
		assertTrue(createdRequestTypeId > 0);
	}
	
	@Test
	public void update() {
		RequestTypeDto updateRequestType = new RequestTypeDto();
		updateRequestType.setId(1L);
		updateRequestType.setDescription("New request type description");
		
		// Validation du code retour
		ClientResponse cr = getWebResource().path("1").type(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class, updateRequestType);
		assertNotNull(cr);
		assertEquals(cr.getClientResponseStatus(), Status.OK);
	}
}

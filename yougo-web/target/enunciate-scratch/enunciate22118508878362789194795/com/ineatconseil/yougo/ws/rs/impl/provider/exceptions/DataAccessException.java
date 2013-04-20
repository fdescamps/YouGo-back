package com.ineatconseil.yougo.ws.rs.impl.provider.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This class is used to provide a convenient way to map a {@link org.springframework.dao.DataAccessException}
 * to a {@link Response} with correct headers (<code>Status.INTERNAL_SERVER_ERROR</code>).
 * 
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 * @see javax.ws.rs.core.Response
 * @see javax.ws.rs.ext.ExceptionMapper
 * @see org.springframework.dao.DataAccessException
 */
@Provider
public class DataAccessException implements
		ExceptionMapper<org.springframework.dao.DataAccessException> {
	
	/**
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	public Response toResponse(org.springframework.dao.DataAccessException ex) {
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).type(MediaType.TEXT_PLAIN)
				.build();
	}
}

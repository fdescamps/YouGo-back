package com.ineatconseil.yougo.ws.rs.impl.provider.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * This class is used to provide a convenient way to map a {@link ObjectRetrievalFailureException}
 * to a {@link Response} with correct headers (<code>Status.NOT_FOUND</code>).
 * 
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 * @see javax.ws.rs.core.Response
 * @see javax.ws.rs.ext.ExceptionMapper
 * @see org.springframework.orm.ObjectRetrievalFailureException
 */
@Provider
public class EntityNotFoundException implements
		ExceptionMapper<ObjectRetrievalFailureException> {
	
	/**
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	public Response toResponse(ObjectRetrievalFailureException ex) {
		return Response.status(Status.NOT_FOUND).entity(ex.getMessage()).type(MediaType.TEXT_PLAIN)
				.build();
	}
}

package com.ineatconseil.yougo.ws.rs.impl.provider.exceptions;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This class is used to provide a convenient way to map a {@link javax.validation.ConstraintViolationException}
 * to a {@link Response} with correct headers (<code>Status.BAD_REQUEST</code>).
 * 
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 * @see javax.ws.rs.core.Response
 * @see javax.ws.rs.ext.ExceptionMapper
 * @see javax.validation.ConstraintViolationException
 */
@Provider
public class ConstraintViolationException implements ExceptionMapper<javax.validation.ConstraintViolationException> {

	/**
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	public Response toResponse(javax.validation.ConstraintViolationException ex) {
		com.ineatconseil.yougo.dto.ConstraintViolation[] violations = new com.ineatconseil.yougo.dto.ConstraintViolation[ex.getConstraintViolations().size()];
		int i = 0;
		
		for(ConstraintViolation<?> csViolation : ex.getConstraintViolations()) {
			com.ineatconseil.yougo.dto.ConstraintViolation violation = new com.ineatconseil.yougo.dto.ConstraintViolation();
			violation.setType(csViolation.getRootBeanClass().getName());
			violation.setField(csViolation.getPropertyPath().toString());
			violation.setMessage(csViolation.getMessage());
			violation.setValue(csViolation.getInvalidValue().toString());
			violations[i++] = violation;
		}
		
		return Response.status(Status.BAD_REQUEST).entity(violations).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
}

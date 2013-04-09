package com.ineatconseil.yougo.ws.rs.impl.provider;

import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.ineatconseil.yougo.dto.ConstraintViolation;
import com.ineatconseil.yougo.dto.DetailedUserDto;
import com.ineatconseil.yougo.dto.RequestDto;
import com.ineatconseil.yougo.dto.RequestStatusDto;
import com.ineatconseil.yougo.dto.RequestTypeDto;
import com.ineatconseil.yougo.dto.UserDto;
import com.ineatconseil.yougo.dto.UserTypeDto;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

/**
 * REST context resolver to convert Java Bean (POJO) from/to available
 * formats hold by the JaxB API. In this case, the JaxB provides a JSON converter.
 * <p>
 * This is pluggable factory that create objects of a specific type, for a
 * certain Java type, and for a specific media type.
 * </p>
 * 
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 * @see javax.ws.rs.ext.ContextResolver
 * @see javax.xml.bind.JAXBContext
 */
@Provider
public class ContextResolver implements javax.ws.rs.ext.ContextResolver<JAXBContext> {

	/**
	 * JaxB entry point for the client.
	 */
	private JAXBContext context;

	/**
	 * Holds the java bean type for conversion from/to JSON representation.
	 */
	private Class<?>[] types = { 
		UserDto.class,
		DetailedUserDto.class,
		RequestDto.class, 
		UserTypeDto.class,
		RequestTypeDto.class,
		RequestStatusDto.class,
		ConstraintViolation.class
	};

	/**
	 * Unique constructor which holds the JSON configuration language.
	 * This one uses the natural JSON language as our inner representation.
	 * 
	 * @throws JAXBException Exception during JAXB initialization.  
	 */
	public ContextResolver() throws JAXBException {
		this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
	}

	/**
	 * @see javax.ws.rs.ext.ContextResolver#getContext(java.lang.Class)
	 */
	public JAXBContext getContext(Class<?> objectType) {
		for (Class<?> type : types) {
			if (type == objectType) {
				return context;
			}
		}

		throw new IllegalStateException("You should never go here !");
	}
}

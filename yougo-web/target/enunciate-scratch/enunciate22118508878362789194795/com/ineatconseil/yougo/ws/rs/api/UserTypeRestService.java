package com.ineatconseil.yougo.ws.rs.api;

import java.util.List;

import javax.ws.rs.core.Response;

import com.ineatconseil.yougo.dto.UserTypeDto;
import com.ineatconseil.yougo.ws.rs.impl.provider.exceptions.DataAccessException;
import com.ineatconseil.yougo.ws.rs.impl.provider.exceptions.EntityNotFoundException;

/**
 * This interface provides typical RESTful services to manage objects of 
 * type {@link UserTypeDto}.
 *  
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 * @see com.ineatconseil.yougo.dto.UserTypeDto
 * @see javax.ws.rs.core.Response
 */
public interface UserTypeRestService {

	/**
	 * This method searches for an {@link UserTypeDto} by
	 * its identifier.
	 * 
	 * @param id The identifier of an {@link UserTypeDto}
	 * @return An {@link UserTypeDto} if exists, else returns a {@link Response} provided by
	 * 		<ul>
	 * 			<li>
	 * 				The {@link EntityNotFoundException} mapper, which returns a <code>NOT_FOUND</code> status code 
	 * 				if no {@link UserTypeDto} matches the given identifier.
	 * 			</li>
	 * 			<li>
	 * 				The {@link DataAccessException} mapper, which returns a <code>INTERNAL_SERVER_ERROR</code> status code
	 * 				in the case of a server error.
	 * 			</li>
	 * 		</ul>
	 */
	UserTypeDto find(Long id);

	/**
	 * This method returns a list of {@link UserTypeDto}.
	 * 
	 * @return A list of all {@link UserTypeDto} or a {@link Response} with
	 * 		an <code>INTERNAL_SERVER_ERROR</code> status code in the case
	 * 		of a server error.
	 */
	List<UserTypeDto> findAll();

	/**
	 * This method provides a way to create a new {@link UserTypeDto}.
	 * <p>
	 * The id attribute of this object may be <code>null</code> or empty.
	 * If it's not the case, a {@link Response} with a <code>BAD_REQUEST</code>
	 * is returned to the client.
	 * </p>
	 * 
	 * @param userTypeDto The new {@link UserTypeDto} to save.
	 * @return A {@link Response} with the status <code>CREATED</code>
	 * 		which holds the URI of the new REST resource.
	 */
	Response save(UserTypeDto userTypeDto);

	/**
	 * This method provides a way to update an {@link UserTypeDto}.
	 * <p>
	 * Both identifiers in method parameters and object's attributes may be
	 * specified to update the given object. If it's not the case, a 
	 * {@link Response} with a <code>BAD_REQUEST</code> is returned to
	 * the client.
	 * </p>
	 * 
	 * @param id The {@link UserTypeDto} identifier.
	 * @param userTypeDto The {@link UserTypeDto} to update.
	 * @return A {@link Response} with the status <code>OK</code>.
	 */
	Response update(Long id, UserTypeDto userTypeDto);

	/**
	 * This method provides a way to delete an {@link UserTypeDto}
	 * by its identifier.
	 * 
	 * @param id The identifier of an {@link UserTypeDto}.
	 * @return A {@link Response} with the status <code>OK</code>
	 */
	Response delete(Long id);
}

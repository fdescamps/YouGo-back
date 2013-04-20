package com.ineatconseil.yougo.ws.rs.api;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.ineatconseil.yougo.dto.RequestDto;
import com.ineatconseil.yougo.dto.RequestStatusDto;

/**
 * This interface provides typical RESTful services to manage objects of 
 * type {@link RequestDto}.
 *  
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 * @see com.ineatconseil.yougo.dto.RequestDto
 * @see javax.ws.rs.core.Response
 */
public interface RequestRestService {

	RequestDto find(SecurityContext securityContext, Long userId, Long id);
	
	List<RequestDto> find(SecurityContext securityContext, Long userId, Date from, Date to, List<RequestStatusDto> requestStatusDtos);

	Response save(Long userId, RequestDto requestDto);

	Response update(SecurityContext securityContext, Long userId, Long id, RequestDto requestDto);

	Response delete(Long id);
}

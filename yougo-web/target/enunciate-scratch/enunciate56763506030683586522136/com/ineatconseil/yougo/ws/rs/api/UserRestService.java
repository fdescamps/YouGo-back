package com.ineatconseil.yougo.ws.rs.api;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.ineatconseil.yougo.dto.UserDto;

public interface UserRestService {

	UserDto find(SecurityContext securityContext, Long id);
	
	UserDto findByEmail(SecurityContext securityContext, String email);

	List<UserDto> findAll();

	Response save(UserDto userDto);

	Response update(SecurityContext securityContext, Long id, UserDto userDto);

	Response delete(SecurityContext securityContext, Long id);
	
	Response updatePassword(SecurityContext securityContext, Long id, String oldPassword, String newPassword, String confirmedNewPassword);
	
}

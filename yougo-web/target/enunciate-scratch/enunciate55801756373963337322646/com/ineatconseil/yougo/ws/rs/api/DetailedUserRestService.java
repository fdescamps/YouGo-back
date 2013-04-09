package com.ineatconseil.yougo.ws.rs.api;

import java.util.Date;
import java.util.List;

import com.ineatconseil.yougo.dto.DetailedUserDto;
import com.ineatconseil.yougo.dto.RequestStatusDto;

public interface DetailedUserRestService {

	List<DetailedUserDto> find(Date from, Date to, List<RequestStatusDto> requestStatusDtos);
	
}

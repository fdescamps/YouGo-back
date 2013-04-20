package com.ineatconseil.yougo.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum RequestStatusDto {
	ACCEPTED,
	PENDING,
	REFUSED;
}

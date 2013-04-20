package com.ineatconseil.yougo.dto;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DetailedUserDto implements Serializable, Comparable<DetailedUserDto> {

	private static final long serialVersionUID = 2336682077912855725L;

	private Long id;
	private String fullName;
	private String email;
	private Boolean admin;
	private Boolean active;
	private Long typeId;
	private Set<RequestDto> requests;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Set<RequestDto> getRequests() {
		return requests;
	}

	public void setRequests(Set<RequestDto> requests) {
		this.requests = requests;
	}

	public int compareTo(DetailedUserDto other) {
		return fullName.compareTo(other.getFullName());
	}

}
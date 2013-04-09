package com.ineatconseil.yougo.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -8920516245625049145L;

	protected BaseEntity() {
	}

	/**
	 * Primary Key
	 */
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

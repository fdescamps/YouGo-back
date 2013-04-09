package com.ineatconseil.yougo.bo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ineatconseil.yougo.bo.listener.BeanValidationEventListener;

@Entity
@Table(name = "request_type")
@EntityListeners(BeanValidationEventListener.class)
public class RequestType extends BaseEntity {

	private static final long serialVersionUID = 3250820664741061732L;
	
	/**
	 * Type de demande (RTT, sans solde etc...)
	 */
	@Basic
	@Column(name = "description", unique = true)
	@NotNull
	@Size(min = 6, max = 64)
	private String description;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}

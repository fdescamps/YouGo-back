package com.ineatconseil.yougo.bo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ineatconseil.yougo.bo.listener.BeanValidationEventListener;

/**
 * This business object represents the type of an user. For example, this user
 * type could be : - A manager - A basic user
 * 
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 * 
 */
@Entity
@Table(name = "user_type")
@EntityListeners(BeanValidationEventListener.class)
public class UserType extends BaseEntity {

	private static final long serialVersionUID = -8069569363560456786L;

	/**
	 * The description of this user type
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

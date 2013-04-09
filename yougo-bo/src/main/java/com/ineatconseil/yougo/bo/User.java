package com.ineatconseil.yougo.bo;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.ineatconseil.yougo.bo.listener.BeanValidationEventListener;

/**
 * This business object represents a user.
 * 
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 */
@Entity
@Table(name = "users")
@EntityListeners(BeanValidationEventListener.class)
public class User extends BaseEntity {

	private static final long serialVersionUID = 3923547705925058440L;

	/**
	 * Full name (ex: Kristina CHUNG)
	 */
	@Basic
	@Column(name = "full_name", unique = true)
	@NotNull
	@Size(min = 6, max = 64)
	private String fullName;

	/**
	 * E-mail (ex: kristina.chung@company.com)
	 */
	@Basic
	@Column(name = "email", unique = true)
	@NotNull
	@Email
	@Size(min = 18, max = 64)
	private String email;

	/**
	 * Password for login authentication
	 */
	@Basic
	@Column(name = "password")
	@NotNull
	@Size(min = 6, max = 32)
	private String password;

	/**
	 * Administrator access
	 */
	@Basic
	@Column(name = "admin")
	private Boolean admin;

	/**
	 * Is the user active ?
	 */
	@Basic
	@Column(name = "active")
	private Boolean active = Boolean.TRUE;

	/**
	 * User's type (can be a manager or a simple user)
	 */
	@ManyToOne
	@JoinColumn(name = "user_type_id", nullable = false)
	@NotNull
	private UserType type;

	/**
	 * The list of requests done by the current user.
	 */
	@OneToMany(mappedBy = "user", cascade = { CascadeType.REMOVE })
	private Set<Request> requests;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public Set<Request> getRequests() {
		return requests;
	}

	public void setRequests(Set<Request> requests) {
		this.requests = requests;
	}
}

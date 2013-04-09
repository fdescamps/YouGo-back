package com.ineatconseil.yougo.bo;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ineatconseil.yougo.bo.listener.BeanValidationEventListener;

/**
 * This business object represents a request for holidays.
 * This object holds many 
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 */
@Entity
@Table(name = "requests")
@EntityListeners(BeanValidationEventListener.class)
public class Request extends BaseEntity {

	private static final long serialVersionUID = -1374967354851164891L;

	/**
	 * Start date for the request
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(name = "from_date", nullable = false)
	private Date from;

	/**
	 * End date for the request
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(name = "to_date", nullable = false)
	private Date to;

	/**
	 * A comment about the request
	 */
	@Basic
	@Column(name = "ask_comment", length = 256)
	private String askComment;

	/**
	 * The answer of the user who change the status of the request
	 */
	@Basic
	@Column(name = "answer_comment", length = 256)
	private String answerComment;

	/**
	 * The type of the request.
	 */
	@ManyToOne
	@JoinColumn(name = "request_type_id", nullable = false)
	@NotNull
	private RequestType type;
	
	/**
	 * The request's status
	 */
	@Basic
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private RequestStatus status = RequestStatus.PENDING;
	
	@Basic
	@ManyToOne
    @JoinColumn(name="user_id")
    @NotNull
	private User user;

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public String getAskComment() {
		return askComment;
	}

	public void setAskComment(String askComment) {
		this.askComment = askComment;
	}

	public String getAnswerComment() {
		return answerComment;
	}

	public void setAnswerComment(String answerComment) {
		this.answerComment = answerComment;
	}

	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

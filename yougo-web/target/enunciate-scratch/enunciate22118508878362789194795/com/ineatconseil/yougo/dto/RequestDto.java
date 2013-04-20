package com.ineatconseil.yougo.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestDto implements Serializable, Comparable<RequestDto> {
	
	private static final long serialVersionUID = -7041385214026193566L;
	
	private Long id;
	private Date from;
	private Date to;
	private String askComment;
	private String answerComment;
	private Long typeId;
	private RequestStatusDto status = RequestStatusDto.PENDING;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public RequestStatusDto getStatus() {
		return status;
	}

	public void setStatus(RequestStatusDto status) {
		this.status = status;
	}

	public int compareTo(RequestDto other) {
		if (from.compareTo(other.getFrom()) == 0) {
			return from.compareTo(other.getFrom());
		} else {
			return to.compareTo(other.getTo());
		}
	}

}

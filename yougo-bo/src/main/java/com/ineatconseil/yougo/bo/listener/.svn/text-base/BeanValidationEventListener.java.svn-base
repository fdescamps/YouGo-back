package com.ineatconseil.yougo.bo.listener;

import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.Hibernate;

public class BeanValidationEventListener {

	@PrePersist
	@PreUpdate
	@PreRemove
	public void validate(Object entity) {
		TraversableResolver tr = new TraversableResolver();
		Validator validator = Validation.buildDefaultValidatorFactory()
				.usingContext().traversableResolver(tr).getValidator();

		final Set<ConstraintViolation<Object>> constraintViolations = validator
				.validate(entity);
		if (constraintViolations.size() > 0) {
			Set<ConstraintViolation<?>> propagatedViolations = new HashSet<ConstraintViolation<?>>(
					constraintViolations.size());
			Set<String> classNames = new HashSet<String>();
			for (ConstraintViolation<?> violation : constraintViolations) {
				propagatedViolations.add(violation);
				classNames.add(violation.getLeafBean().getClass().getName());
			}
			StringBuilder builder = new StringBuilder();
			builder.append("Validation failed for classes ");
			builder.append(classNames);
			throw new ConstraintViolationException(builder.toString(),
					propagatedViolations);
		}
	}

	class TraversableResolver implements javax.validation.TraversableResolver {

		public boolean isReachable(Object traversableObject,
				Path.Node traversableProperty, Class<?> rootBeanType,
				Path pathToTraversableObject, ElementType elementType) {
			return traversableObject == null
					|| Hibernate.isInitialized(traversableObject);
		}

		public boolean isCascadable(Object traversableObject,
				Path.Node traversableProperty, Class<?> rootBeanType,
				Path pathToTraversableObject, ElementType elementType) {
			return true;
		}
	}
}

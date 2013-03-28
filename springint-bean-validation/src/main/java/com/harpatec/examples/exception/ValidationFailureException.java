package com.harpatec.examples.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class ValidationFailureException extends Exception {

	private final Set<ConstraintViolation<Object>> violations;
	
	public ValidationFailureException(Set<ConstraintViolation<Object>> violations) {
		super();
		this.violations = violations;
	}

	public Set<ConstraintViolation<Object>> getViolations() {
		return violations;
	}

	public List<String> getFailureMessages() {
		List<String> failureMessages = new ArrayList<String>();
		if (violations != null) {
			for (ConstraintViolation violation : violations) {
				StringBuilder message = new StringBuilder();
				message.append("[");
				message.append(violation.getPropertyPath());
				message.append("] ");
				message.append(violation.getMessage());
				failureMessages.add(message.toString());
			}
		}
		return failureMessages;
	}

	private static final long serialVersionUID = -3345328797047598048L;

}

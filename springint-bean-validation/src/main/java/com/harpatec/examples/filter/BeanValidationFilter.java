package com.harpatec.examples.filter;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.integration.annotation.Filter;
import org.springframework.util.Assert;

import com.harpatec.examples.exception.ValidationFailureException;

public class BeanValidationFilter {

	private final Validator validator;
	private boolean validationFailureThrowsException;

	public BeanValidationFilter(Validator validator) {
		this.validator = validator;
		Assert.notNull(validator, "Validator object cannot be null");
	}

	@Filter
	public boolean accept(Object bean) throws ValidationFailureException {
		
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean);

		if (constraintViolations.size() > 0) {
			if (validationFailureThrowsException) {
				throw new ValidationFailureException(constraintViolations);
			}
			return false;
		}
		
		return true;
	}

	public void setValidationFailureThrowsException(boolean validationFailureThrowsException) {
		this.validationFailureThrowsException = validationFailureThrowsException;
	}
	
}

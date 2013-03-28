package com.harpatec.examples.filter;

import static org.junit.Assert.assertFalse;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.harpatec.examples.bean.Person;
import com.harpatec.examples.exception.ValidationFailureException;

public class BeanValidationFilterTest {

	private BeanValidationFilter validationFilter;
	
	private static Validator validator;

	@BeforeClass
	public static void setUpBeforeClass() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Before
	public void setUp() throws Exception {
		validationFilter = new BeanValidationFilter(validator);
	}

	@Test
	public void test() throws ValidationFailureException {
		Person person = new Person();
		person.setFirstName("");
		person.setLastName("Jones");
		
		assertFalse("BeanValidationFilter should fire for invalid Person bean", validationFilter.accept(person));

	}

}

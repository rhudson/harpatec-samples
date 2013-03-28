/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.harpatec.examples.app;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.MessageHandlingException;

import com.harpatec.examples.bean.Address;
import com.harpatec.examples.bean.Person;
import com.harpatec.examples.exception.ValidationFailureException;
import com.harpatec.examples.service.BeanValidationService;


/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Robert Hudson
 * @since 1.0
 *
 */
public final class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);

	private Main() { }

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		final BeanValidationService service = context.getBean(BeanValidationService.class);
		
		//
		// Validate a Person bean that will pass
		//
		Person person = new Person();
		person.setFirstName("Davey");
		person.setLastName("Jones");

		validateObject(service, person);


		//
		// Validate a Person bean that will fail
		//
		person = new Person();
		person.setFirstName("");
		person.setLastName("Jones");

		validateObject(service, person);
		
		//
		// Validate an Address bean that will pass
		//
		Address address = new Address();
		address.setStreetNumber("12345");
		address.setStreetName("Main St.");
		address.setCity("Anytown");
		address.setState("CA");
		address.setZipCode("70808");

		validateObject(service, address);

		//
		// Validate an Address bean that will fail
		//

		address = new Address();
		address.setStreetNumber(null);
		address.setStreetName("");
		address.setCity("Anytown");
		address.setState("CA2");
		address.setZipCode("70808123123");

		validateObject(service, address);
		
		System.exit(0);

	}

	private static void validateObject(BeanValidationService service, Object bean) {

		try {
			LOGGER.info("Submitting Object for validation: " + bean);
			Object result = service.validate(bean);
			LOGGER.info("Validation passed for object: " + bean);

		} catch (MessageHandlingException e) {
			processException(e);
		}
		
	}

	private static void processException(MessageHandlingException e) {
		LOGGER.info("Received exception!!!");
		Throwable cause = e.getCause();
		if (cause instanceof ValidationFailureException) {
			ValidationFailureException failureException = (ValidationFailureException) cause;
			LOGGER.info("Validation Error messages:");
			for (String message : failureException.getFailureMessages()) {
				LOGGER.info("\t" + message);
			}
		}
	}
}

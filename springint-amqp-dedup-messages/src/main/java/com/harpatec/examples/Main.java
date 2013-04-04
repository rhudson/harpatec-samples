/*
 * Copyright 2002-2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.harpatec.examples;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Order;

import com.harpatec.examples.model.MessageRecord;

/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 * 
 * @author Robert Hudson
 * @since 1.0
 * 
 */
public final class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	private Main() {
	}

	/**
	 * Load the Spring Integration Application Context
	 * 
	 * @param args - command line arguments
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static void main(final String... args) throws InterruptedException, JsonGenerationException,
			JsonMappingException, IOException {

		final AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		LOGGER.debug("Dropping the collection of MessageRecords");
		MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);
		mongoTemplate.dropCollection(MessageRecord.class);
		mongoTemplate.indexOps(MessageRecord.class).ensureIndex(new Index().on("key", Order.ASCENDING).unique());
		mongoTemplate.indexOps(MessageRecord.class).ensureIndex(new Index().on("completionTime", Order.ASCENDING));

		RabbitTemplate inboundTemplate = (RabbitTemplate) context.getBean("amqpTemplateInbound");
		Map<String, Object> messageMap = new HashMap<String, Object>();
		messageMap.put("count", "4");

		LOGGER.debug("Submitting first message which should pass DuplicateMessageFilter ok.");
		submitMessage(inboundTemplate, messageMap);

		Thread.sleep(5 * 1000);
		LOGGER.debug("Submitting a duplicate message which should get caught by the DuplicateMessageFilter.");
		submitMessage(inboundTemplate, messageMap);

		Thread.sleep(6 * 60 * 1000);

		System.exit(0);

	}

	private static void submitMessage(RabbitTemplate inboundTemplate, Map<String, Object> messageMap)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String payload = mapper.writeValueAsString(messageMap);

		LOGGER.debug("Writing message to inbound queue\n" + payload);
		inboundTemplate.convertAndSend(payload);

	}
}

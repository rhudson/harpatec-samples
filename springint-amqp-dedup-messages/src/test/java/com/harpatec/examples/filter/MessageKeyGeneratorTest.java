package com.harpatec.examples.filter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.integration.support.MessageBuilder;

public class MessageKeyGeneratorTest {

	private static final String QUEUE_NAME = "queueName101";

	private static final String PAYLOAD = "MessagePayload-BlahBlah";

	private static final String CLIENT_ID = "clientId101";

	private MessageKeyGenerator generator;

	@Before
	public void setUp() throws Exception {
		generator = new MessageKeyGenerator();
	}

	@Test
	public void testBuildKey() {
		MessageBuilder<String> builder = MessageBuilder.withPayload(PAYLOAD);
		builder.setHeader(DuplicateMessageFilter.QUEUE_NAME, QUEUE_NAME);
		builder.setHeader("clientId", CLIENT_ID);

		List<String> headersInKey = new ArrayList<String>();
		headersInKey.add(DuplicateMessageFilter.QUEUE_NAME);
		headersInKey.add("clientId");
		generator.setHeadersInKey(headersInKey);

		assertEquals("Generated key should match", "518e688d3a12a0501e483643426b86d0a18157dc",
				generator.buildKey(builder.build()));
	}

}

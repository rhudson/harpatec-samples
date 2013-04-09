package com.harpatec.examples.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageTransformer.class);

	public List<String> transform(Map<String, Object> payload) throws InterruptedException {

		List<String> results = new ArrayList<String>();

		int count = 0;

		if (payload.get("fail") != null && "true".equalsIgnoreCase(payload.get("fail").toString())) {
			throw new RuntimeException("Failing as requested by incoming message!");
		}

		if (payload.get("count") != null) {
			count = Integer.parseInt(payload.get("count").toString());
		}
		
		LOGGER.debug("Splitting into {} messages.", count);

		int timeout = 30;
		LOGGER.debug("This may take " + timeout + " seconds...");

		Thread.sleep(timeout * 1000);

		for (int current = 0; current < count; current++) {
			results.add("Hello World [" + (current + 1) + "/" + count + "]");
		}

		LOGGER.debug("Done with the split.");

		return results;

	}

}

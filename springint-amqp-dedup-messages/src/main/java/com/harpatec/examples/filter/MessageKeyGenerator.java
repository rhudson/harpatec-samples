package com.harpatec.examples.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.integration.Message;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class MessageKeyGenerator {

	private String queueName;
	
	private List<String> headersInKey = new ArrayList<String>();

	public String buildKey(Message<String> message) {
		return buildKeyFromPayload(message.getHeaders(), message.getPayload());
	}

	public String buildKeyFromPayload(Map<String, Object> messageHeaders, String payload) {

		Hasher hasher = Hashing.sha1().newHasher();

		if (queueName != null) {
			hasher.putString(queueName);
		}

		for (String header : headersInKey) {
			if (messageHeaders.containsKey(header)) {
				hasher.putString(messageHeaders.get(header).toString());
			}
		}

		hasher.putString(payload);

		return hasher.hash().toString();
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void setHeadersInKey(List<String> headersInKey) {
		this.headersInKey = headersInKey;
	}

}

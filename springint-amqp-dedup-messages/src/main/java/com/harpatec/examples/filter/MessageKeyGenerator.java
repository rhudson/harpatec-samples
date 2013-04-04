package com.harpatec.examples.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.integration.Message;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class MessageKeyGenerator {

	private List<String> headersInKey = new ArrayList<String>();

	public String buildKey(Message<String> message) {

		Hasher hasher = Hashing.sha1().newHasher();

		for (String header : headersInKey) {
			if (message.getHeaders().containsKey(header)) {
				hasher.putString(message.getHeaders().get(header).toString());
			}
		}

		hasher.putString(message.getPayload());

		return hasher.hash().toString();
	}

	public void setHeadersInKey(List<String> headersInKey) {
		this.headersInKey = headersInKey;
	}

}

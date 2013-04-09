package com.harpatec.examples.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.handler.advice.AbstractRequestHandlerAdvice;

import com.harpatec.examples.repository.MessageRepository;

public class MessageKeyGeneratorAdvice extends AbstractRequestHandlerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageKeyGeneratorAdvice.class);

	@Autowired
	private MessageRepository messageRepository;

	private MessageKeyGenerator messageKeyGenerator;

	private DuplicateMessageFilter messageFilter;

	@Override
	protected Object doInvoke(ExecutionCallback callback, Object target, Message<?> message) throws Exception {
		String messageKey = null;
		boolean acceptMessage = true;

		String payload = extractPayload(message);

		if (payload != null) {
			messageKey = messageKeyGenerator.buildKeyFromPayload(message.getHeaders(), payload);
			acceptMessage = messageFilter.acceptPayload(messageKey, messageKeyGenerator.getQueueName(), payload);
		}

		Object result = null;

		if (acceptMessage) {
			result = callback.execute();

			if (messageKey != null) {
				LOGGER.debug("Setting message as complete.  key: [{}]", messageKey);
				messageRepository.setCompleted(messageKey);
			}

		}

		return result;
	}

	private String extractPayload(Message<?> message) {

		if (message.getPayload() instanceof String) {
			return (String) message.getPayload();
		}

		if (message.getPayload() instanceof byte[]) {
			return new String((byte[]) message.getPayload());
		}

		return null;
	}

	public void setMessageKeyGenerator(MessageKeyGenerator messageKeyGenerator) {
		this.messageKeyGenerator = messageKeyGenerator;
	}

	public void setMessageFilter(DuplicateMessageFilter messageFilter) {
		this.messageFilter = messageFilter;
	}

}

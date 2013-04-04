package com.harpatec.examples.filter;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.Filter;
import org.springframework.util.Assert;

import com.harpatec.examples.model.MessageRecord;
import com.harpatec.examples.repository.MessageRepository;

public class DuplicateMessageFilter implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(DuplicateMessageFilter.class);

	public static final String MESSAGE_KEY = "messageKey";
	public static final String QUEUE_NAME = "queueName";

	@Autowired
	private MessageRepository messageRepository;

	private boolean recordPayload;

	@Filter
	public boolean accept(Message<String> message) {

		MessageRecord messageRecord = new MessageRecord();
		messageRecord.setKey((String) message.getHeaders().get(MESSAGE_KEY));
		messageRecord.setQueueName((String) message.getHeaders().get(QUEUE_NAME));
		messageRecord.setCompletionTime(null);
		messageRecord.setReceiveCount(1);
		messageRecord.getReceiveHistory().add(new DateTime());

		if (recordPayload) {
			messageRecord.setPayload(message.getPayload());
		}

		try {
			messageRepository.save(messageRecord);
			LOGGER.debug("Message saved. key: [{}]", messageRecord.getKey());

		} catch (DuplicateKeyException dupKeyExc) {
			LOGGER.warn("Filtering out duplicate message. key: [{}]", messageRecord.getKey());
			recordEvent(messageRecord.getKey());
			return false;
		}

		return true;
	}

	private void recordEvent(String key) {
		messageRepository.addToHistory(key, new DateTime());
	}

	public void setMessageRepository(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public void setRecordPayload(boolean recordPayload) {
		this.recordPayload = recordPayload;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(messageRepository, "messageRepository is a required dependency for DuplicateMessageFilter");
	}

}

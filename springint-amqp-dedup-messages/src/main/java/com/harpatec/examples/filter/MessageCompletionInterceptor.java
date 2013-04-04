package com.harpatec.examples.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter;

import com.harpatec.examples.repository.MessageRepository;

public class MessageCompletionInterceptor extends ChannelInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageCompletionInterceptor.class);

	@Autowired
	private MessageRepository messageRepository;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		String messageKey = (String) message.getHeaders().get(DuplicateMessageFilter.MESSAGE_KEY);

		LOGGER.debug("Setting message as complete.  key: [{}]", messageKey);
		messageRepository.setCompleted(messageKey);

		return message;
	}

}

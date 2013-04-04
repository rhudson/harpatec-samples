package com.harpatec.examples.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.integration.support.MessageBuilder;

import com.harpatec.examples.model.MessageRecord;
import com.harpatec.examples.repository.MessageRepository;

public class DuplicateMessageFilterTest {

	private static final String QUEUE_NAME_VALUE = "queueName101";
	private static final String MESSAGE_KEY_VALUE = "messageKey1010101";

	private static final String PAYLOAD = "MessagePayload-BlahBlah";

	private DuplicateMessageFilter messageFilter;

	@Mock
	private MessageRepository messageRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		messageFilter = new DuplicateMessageFilter();
		messageFilter.setMessageRepository(messageRepository);
		messageFilter.afterPropertiesSet();
	}

	@Test
	public void testAcceptWithPayload() {
		messageFilter.setRecordPayload(true);
		MessageRecord record = testAccept();
		assertNotNull("Payload should be included", record.getPayload());
		assertEquals("Payload should be written to the repository", PAYLOAD, record.getPayload());
	}

	@Test
	public void testAcceptWithoutPayload() {
		MessageRecord record = testAccept();
		assertNull("Payload should be excluded", record.getPayload());
	}

	public MessageRecord testAccept() {
		MessageBuilder<String> builder = MessageBuilder.withPayload(PAYLOAD);
		builder.setHeader(DuplicateMessageFilter.QUEUE_NAME, QUEUE_NAME_VALUE);
		builder.setHeader(DuplicateMessageFilter.MESSAGE_KEY, MESSAGE_KEY_VALUE);

		//
		// Run the method to test
		//

		messageFilter.accept(builder.build());

		ArgumentCaptor<MessageRecord> recordCaptor = ArgumentCaptor.forClass(MessageRecord.class);
		verify(messageRepository).save(recordCaptor.capture());
		verify(messageRepository, never()).addToHistory(any(String.class), any(DateTime.class));

		MessageRecord record = recordCaptor.getValue();

		assertEquals("Queue name should be written to the repository", QUEUE_NAME_VALUE, record.getQueueName());
		assertEquals("Message key should be written to the repository", MESSAGE_KEY_VALUE, record.getKey());

		return record;
	}

	@Test
	public void testReject() {
		when(messageRepository.save(any(MessageRecord.class))).thenThrow(new DuplicateKeyException(""));

		MessageBuilder<String> builder = MessageBuilder.withPayload(PAYLOAD);
		builder.setHeader(DuplicateMessageFilter.QUEUE_NAME, QUEUE_NAME_VALUE);
		builder.setHeader(DuplicateMessageFilter.MESSAGE_KEY, MESSAGE_KEY_VALUE);

		//
		// Run the method to test
		//
		messageFilter.accept(builder.build());

		verify(messageRepository).save(any(MessageRecord.class));
		verify(messageRepository).addToHistory(eq(MESSAGE_KEY_VALUE), any(DateTime.class));

	}

}

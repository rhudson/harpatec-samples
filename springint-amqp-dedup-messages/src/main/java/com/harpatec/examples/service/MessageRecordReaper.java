package com.harpatec.examples.service;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.harpatec.examples.repository.MessageRepository;

@Service("messageRecordReaper")
public class MessageRecordReaper {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageRecordReaper.class);

	// Run the service every 20 seconds
	private static final int SCHEDULE_PERIOD_IN_SECONDS = 20;

	// Set the age cutoff of completed MessageRecord objects to 1 minute ago
	private static final int CUTOFF_SECONDS_AGO = 1 * 60;

	@Autowired
	private MessageRepository messageRepository;

	@Scheduled(fixedRate = SCHEDULE_PERIOD_IN_SECONDS * 1000)
	public void cleanupOldMessageRecords() {
		DateTime cutoffTime = (new DateTime(DateTimeZone.UTC)).minusSeconds(CUTOFF_SECONDS_AGO);
		LOGGER.debug("Removing completed MessageRecords older than cutoff [{}]", cutoffTime);
		messageRepository.removeOldRecords(cutoffTime);
	}

}

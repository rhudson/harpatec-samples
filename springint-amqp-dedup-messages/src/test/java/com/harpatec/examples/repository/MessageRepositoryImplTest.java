package com.harpatec.examples.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.harpatec.examples.model.MessageRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MessageRepositoryImplTest {

	@Autowired
	private MessageRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void setUp() {
		repository.deleteAll();
	}

	@After
	public void cleanup() {
		mongoTemplate.getDb().dropDatabase();
	}

	@Test
	public void testRemoveOldRecords() {
		DateTime cutoffTime;

		addSeedData();
		assertThat(mongoTemplate.count(null, MessageRecord.class), is(5L));

		cutoffTime = new DateTime(2013, 4, 3, 16, 8, 0);
		repository.removeOldRecords(cutoffTime);
		assertThat(mongoTemplate.count(null, MessageRecord.class), is(2L));
	}

	private void addSeedData() {
		List<MessageRecord> list = new ArrayList<MessageRecord>();
		MessageRecord record;

		record = new MessageRecord();
		record.setCompletionTime(new DateTime(2012, 12, 12, 17, 10, 10));
		list.add(record);

		record = new MessageRecord();
		record.setCompletionTime(new DateTime(2013, 4, 3, 16, 7, 0));
		list.add(record);

		record = new MessageRecord();
		record.setCompletionTime(new DateTime(2013, 4, 3, 16, 7, 59));
		list.add(record);

		record = new MessageRecord();
		record.setCompletionTime(new DateTime(2013, 4, 3, 16, 8, 0));
		list.add(record);

		record = new MessageRecord();
		record.setCompletionTime(new DateTime(2013, 4, 3, 16, 9, 0));
		list.add(record);

		repository.save(list);

	}

}

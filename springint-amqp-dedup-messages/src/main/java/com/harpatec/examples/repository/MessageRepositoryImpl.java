package com.harpatec.examples.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.harpatec.examples.model.MessageRecord;

public class MessageRepositoryImpl implements CustomMessageRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addToHistory(String key, DateTime dateTime) {
		Query query = new Query(Criteria.where("key").is(key));
		Update update = (new Update()).inc("receiveCount", 1).push("receiveHistory", dateTime);
		mongoTemplate.findAndModify(query, update, MessageRecord.class);
	}

	@Override
	public void setCompleted(String key) {
		Query query = new Query(Criteria.where("key").is(key));
		Update update = (new Update()).set("completionTime", new DateTime());
		mongoTemplate.findAndModify(query, update, MessageRecord.class);
	}

	@Override
	public void removeOldRecords(DateTime cutoffTime) {
		Criteria criteria = where("completionTime").lt(cutoffTime);
		mongoTemplate.remove(query(criteria), MessageRecord.class);
	}

}

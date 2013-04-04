package com.harpatec.examples.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.harpatec.examples.model.MessageRecord;

@Repository
public interface MessageRepository extends MongoRepository<MessageRecord, String>, CustomMessageRepository {

}

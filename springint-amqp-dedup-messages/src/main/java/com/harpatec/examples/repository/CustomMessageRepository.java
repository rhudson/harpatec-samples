package com.harpatec.examples.repository;

import org.joda.time.DateTime;

public interface CustomMessageRepository {

	void addToHistory(String key, DateTime dateTime);

	void setCompleted(String key);

	void removeOldRecords(DateTime cutoffTime);
}

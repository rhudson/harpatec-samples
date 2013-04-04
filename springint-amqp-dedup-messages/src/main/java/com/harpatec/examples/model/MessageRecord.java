package com.harpatec.examples.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messageHistory")
public class MessageRecord {

	@Id
	private String id;

	private String key;

	private String queueName;

	private int receiveCount;

	private List<DateTime> receiveHistory = new ArrayList<DateTime>();

	private DateTime completionTime;

	private String payload;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public long getReceiveCount() {
		return receiveCount;
	}

	public void setReceiveCount(int receiveCount) {
		this.receiveCount = receiveCount;
	}

	public List<DateTime> getReceiveHistory() {
		return receiveHistory;
	}

	public void setReceiveHistory(List<DateTime> receiveHistory) {
		this.receiveHistory = receiveHistory;
	}

	public DateTime getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(DateTime completionTime) {
		this.completionTime = completionTime;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}

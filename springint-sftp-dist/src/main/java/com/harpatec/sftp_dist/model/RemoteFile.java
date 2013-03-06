/*
 * Copyright 2002-2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.harpatec.sftp_dist.model;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "remoteFiles")
@CompoundIndexes({ @CompoundIndex(name = "host_name_idx", def = "{'host': 1, 'name': 1}", unique = true) })
public class RemoteFile {

	private String host;
	private String name;
	private DateTime createdDtm;
	private DateTime processedDtm;
	private String processingHostKey;
	private String status;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateTime getCreatedDtm() {
		return createdDtm;
	}

	public void setCreatedDtm(DateTime createdDtm) {
		this.createdDtm = createdDtm;
	}

	public DateTime getProcessedDtm() {
		return processedDtm;
	}

	public void setProcessedDtm(DateTime processedDtm) {
		this.processedDtm = processedDtm;
	}

	public String getProcessingHostKey() {
		return processingHostKey;
	}

	public void setProcessingHostKey(String processingHostKey) {
		this.processingHostKey = processingHostKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

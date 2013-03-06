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

package com.harpatec.sftp_dist.filter;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.integration.file.filters.AbstractFileListFilter;
import org.springframework.util.Assert;

import com.harpatec.sftp_dist.model.RemoteFile;
import com.harpatec.sftp_dist.repository.RemoteFileRepository;
import com.jcraft.jsch.ChannelSftp.LsEntry;

public class DataRepositoryLockingSftpLsEntryFilter extends AbstractFileListFilter<LsEntry> implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataRepositoryLockingSftpLsEntryFilter.class);

	@Autowired
	private RemoteFileRepository repository;

	private String remoteHostName;

	@Override
	protected boolean accept(LsEntry file) {
		if (".".equals(file.getFilename()) || "..".equals(file.getFilename())) {
			return false;
		}
		RemoteFile remoteFile = new RemoteFile();
		remoteFile.setHost(remoteHostName);
		remoteFile.setName(file.getFilename());
		remoteFile.setCreatedDtm(new DateTime());
		remoteFile.setStatus("DOWNLOADING");

		try {
			repository.save(remoteFile);

		} catch (DuplicateKeyException dupKeyExc) {
			LOGGER.info("Could not acquire lock to download file [{}] [{}]", remoteFile.getHost(), remoteFile.getName());
			return false;
		}

		return true;
	}

	public void setRepository(RemoteFileRepository repository) {
		this.repository = repository;
	}

	public void setRemoteHostName(String remoteHostName) {
		this.remoteHostName = remoteHostName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.repository, "repository is a required property.");
	}

}

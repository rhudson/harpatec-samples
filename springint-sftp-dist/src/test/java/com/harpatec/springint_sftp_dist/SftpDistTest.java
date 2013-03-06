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

package com.harpatec.springint_sftp_dist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.harpatec.sftp_dist.model.RemoteFile;
import com.harpatec.sftp_dist.repository.RemoteFileRepository;

/**
 * @author Robert Hudson
 * @since 1.0
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SftpDistTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SftpDistTest.class);

	private static final String INCOMING_FILE = "incoming/example.txt";

	@Autowired
	private RemoteFileRepository repository;

	private final File incomingFile = new File(INCOMING_FILE);

	@Before
	public void setup() {
		repository.deleteAll();

		if (incomingFile.exists()) {
			incomingFile.delete();
		}
	}

	@Test
	public void testSpringIntegrationContextStartup() throws Exception {

		int waitPeriod = 10000;
		LOGGER.debug("Sleeping for {} seconds to allow transfer to complete.", waitPeriod / 1000);
		Thread.sleep(waitPeriod);
		LOGGER.debug("Done sleeping.");

		assertTrue("Should have been able to download " + INCOMING_FILE, incomingFile.exists());
		
		Collection<RemoteFile> records = repository.findByHostAndName("remoteHost1", "example.txt");
		assertNotNull("There should be a lock records in the repository", records);
		assertEquals("There should be a lock records in the repository", 1, records.size());
	}

}

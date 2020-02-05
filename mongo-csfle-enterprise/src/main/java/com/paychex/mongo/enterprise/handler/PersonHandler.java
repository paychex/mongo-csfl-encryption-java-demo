
/*
 * # Copyright 2020 Paychex, Inc.
 * # Licensed pursuant to the terms of the Apache License, Version 2.0 (the "License");
 * # your use of the Work is subject to the terms and conditions of the License.
 * # You may obtain a copy of the License at
 * #
 * # http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Disclaimer of Warranty. Unless required by applicable law or agreed to in writing, Licensor
 * # provides the Work (and each Contributor provides its Contributions) on an "AS IS" BASIS,
 * # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including,
 * # without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT,
 * # MERCHANTABILITY, OR FITNESS FOR A PARTICULAR PURPOSE. You are solely responsible
 * # for determining the appropriateness of using or redistributing the Work and assume
 * # any risks associated with your exercise of permissions under this License.
 */

package com.paychex.mongo.enterprise.handler;

import com.paychex.mongo.enterprise.entity.AutoEncryptedPerson;
import com.paychex.mongo.enterprise.respository.EncryptedPersonRepository;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
	Author: Visweshwar Ganesh
	created on 1/21/20
*/
@Component
public class PersonHandler {

	private static final Logger logger = LoggerFactory.getLogger(PersonHandler.class);
	@Autowired
	private EncryptedPersonRepository encryptedPersonRepository;

	private void clean() {
		encryptedPersonRepository.deleteAll();
	}

	public void runApplication() {
		clean();
		//Create a couple of non encrypted persons
		AutoEncryptedPerson p1 = new AutoEncryptedPerson("Alice", "Smith",113431222,"+1-114-114-1250","B+" );
		AutoEncryptedPerson p2 = new AutoEncryptedPerson("Bob", "Smith",113771224,"+1-114-114-1251","O+");

		//Save persons..
		encryptedPersonRepository.saveAll(Arrays.asList(new AutoEncryptedPerson[]{p1,p2}));

		// fetch all persons
		logger.debug("Persons found with findAll():");
		logger.debug("-------------------------------");

		List<AutoEncryptedPerson> persons = encryptedPersonRepository.findAll();

		for (AutoEncryptedPerson autoEncryptedPerson : persons ) {
			logger.debug(autoEncryptedPerson.toString());
		}

		// fetch an individual customer
		logger.debug("Person found with findByFirstName('Alice'):");
		logger.debug("--------------------------------");

		AutoEncryptedPerson findByFirstNamePerson = encryptedPersonRepository.findByFirstName("Alice");
		logger.info("findByFirstNamePerson Equals Alice Success: {}" ,findByFirstNamePerson.getFirstName().equals("Alice"));

		//For Find by SSN we have to first get the binary version of SSN
		AutoEncryptedPerson findBySsn = encryptedPersonRepository.findBySsn(113431222);
		logger.info("findBySsn equals Alice Success: {}" ,(findBySsn).getFirstName().equals("Alice"));

	}
}

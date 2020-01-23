
/*
 * # Copyright 2019 Paychex, Inc.
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

package com.paychex.mongo.community.handler;

import com.paychex.mongo.community.entity.EncryptedPerson;
import com.paychex.mongo.community.entity.Person;
import com.paychex.mongo.community.entity.PersonEntityHelper;
import com.paychex.mongo.community.respository.EncryptedPersonRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

	@Autowired
	private PersonEntityHelper personEntityHelper;

	private void clean() {
		encryptedPersonRepository.deleteAll();
	}

	public void runApplication() {
		clean();
		//Create a couple of non encrypted persons
		Person p1 = new Person("Alice", "Smith",113431222,"+1-114-114-1250","B+" );
		Person p2 = new Person("Bob", "Smith",113771224,"+1-114-114-1251","O+");

		//Encrypt the Person and save to EncryptedPerson
		EncryptedPerson ep1 = personEntityHelper.getEncrypedPerson(p1);
		EncryptedPerson ep2 = personEntityHelper.getEncrypedPerson(p2);
		//Save persons..
		encryptedPersonRepository.saveAll(Arrays.asList(new EncryptedPerson[]{ep1,ep2}));

		// fetch all persons
		logger.debug("Persons found with findAll():");
		logger.debug("-------------------------------");

		List<Person> decryptedPersons = encryptedPersonRepository.findAll()
				.stream().map(ep -> personEntityHelper.getPerson(ep))
				.collect(Collectors.toList());

		for (Person person : decryptedPersons ) {
			logger.debug(person.toString());
		}

		// fetch an individual customer
		logger.debug("Person found with findByFirstName('Alice'):");
		logger.debug("--------------------------------");

		EncryptedPerson findByFirstNamePerson = encryptedPersonRepository.findByFirstName("Alice");
		logger.info("findByFirstNamePerson Equals Alice Success: {}" ,findByFirstNamePerson.getFirstName().equals("Alice"));

		//For Find by SSN we have to first get the binary version of SSN
		EncryptedPerson findBySsn = encryptedPersonRepository.findBySsn(personEntityHelper.getEncryptedSsn(113431222));
		logger.info("findBySsn equals Alice Success: {}" ,personEntityHelper.getPerson(findBySsn).getFirstName().equals("Alice"));

	}
}

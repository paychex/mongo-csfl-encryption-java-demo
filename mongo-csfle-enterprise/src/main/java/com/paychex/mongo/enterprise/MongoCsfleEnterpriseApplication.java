
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

package com.paychex.mongo.enterprise;

import com.paychex.mongo.enterprise.handler.PersonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
	Author: Visweshwar Ganesh
	created on 1/21/20 10:23 AM
*/
@SpringBootApplication
public class MongoCsfleEnterpriseApplication implements CommandLineRunner {

	@Autowired
	private PersonHandler handler;

	Logger logger = LoggerFactory.getLogger(MongoCsfleEnterpriseApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(MongoCsfleEnterpriseApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		handler.runApplication();
	}

}


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

package com.paychex.mongo.community.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import com.mongodb.internal.build.MongoDriverVersion;
import com.paychex.mongo.community.keymangement.KMSHandler;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import static java.lang.String.format;
import static java.lang.System.getProperty;

/*
	Author: Visweshwar Ganesh
	created on 1/21/20
*/
@Configuration
@EnableMongoRepositories(basePackages = "com.paychex.mongo.community.respository")
public class MongoConfig extends AbstractMongoClientConfiguration {


	@Value(value = "${spring.data.mongodb.database}")
	private String DB_DATABASE;
	@Value(value = "${spring.data.mongodb.uri}")
	private String DB_CONNECTION;
	@Autowired
	private KMSHandler kmsHandler;



	private MongoDriverInformation  getMongoDriverInfo(){
		return MongoDriverInformation.builder()
				.driverName(MongoDriverVersion.NAME)
				.driverVersion(MongoDriverVersion.VERSION)
				.driverPlatform(format("Java/%s/%s", getProperty("java.vendor", "unknown-vendor"),
						getProperty("java.runtime.version", "unknown-version")))
				.build();
	}

	private MongoClientSettings getMongoClientSettings(){

		kmsHandler.buildOrValidateVault();
		return MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(DB_CONNECTION))
				.build();
	}

	/**
	 * Returns the list of custom converters that will be used by the MongoDB template
	 *
	 **/
	public CustomConversions customConversions() {
		CustomConversions customConversions = new MongoCustomConversions(
				Arrays.asList(new BinaryToBsonBinaryConverter(),
						new BsonBinaryToBinaryConverter()));
		return customConversions;
	}


	@Override
	public MongoClient mongoClient() {
		kmsHandler.buildOrValidateVault();
		MongoClient mongoClient = new MongoClientImpl(getMongoClientSettings(),getMongoDriverInfo());
		return mongoClient;
	}

	@Override
	protected String getDatabaseName() {
		return DB_DATABASE;
	}
}


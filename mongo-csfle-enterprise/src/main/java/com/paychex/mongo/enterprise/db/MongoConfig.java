
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

package com.paychex.mongo.enterprise.db;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import com.mongodb.internal.build.MongoDriverVersion;
import com.paychex.mongo.enterprise.entity.PersonEncryptionSchema;
import com.paychex.mongo.enterprise.keymangement.KMSHandler;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bson.BsonDocument;
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
@EnableMongoRepositories(basePackages = "com.paychex.mongo.enterprise.respository")
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


	private MongoClientSettings getAutoEncryptMongoClientSettings(){

		return MongoClientSettings.builder()
				.autoEncryptionSettings(autoEncryptionSettings())
				.applyConnectionString(new ConnectionString(DB_CONNECTION))
				.build();
	}


	private AutoEncryptionSettings autoEncryptionSettings(){

		return AutoEncryptionSettings.builder()
				.kmsProviders(kmsHandler.getKMSMap())
				.extraOptions(kmsHandler.getExtraOptsMap())
				.keyVaultNamespace(kmsHandler.getEncryptionCollectionName())
				.schemaMap(getJsonSchemaMap())
				.build();
	}


	private Map<String, BsonDocument> getJsonSchemaMap(){

		return Stream.of(
				new AbstractMap.SimpleEntry<>("test.autoEncryptedPerson",

						BsonDocument.parse(PersonEncryptionSchema.getDocument(kmsHandler.getEncryptionKeyBase64()).toJson())
				)
		).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
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
		MongoClient mongoClient = new MongoClientImpl(getAutoEncryptMongoClientSettings(),getMongoDriverInfo());
		CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions();
		ValidationOptions validationOptions = new ValidationOptions();
		validationOptions.validator(PersonEncryptionSchema.getDocument(kmsHandler.getEncryptionKeyBase64()));
		createCollectionOptions.validationOptions(validationOptions);


		return mongoClient;
	}

	@Override
	protected String getDatabaseName() {
		return DB_DATABASE;
	}
}


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

package com.paychex.mongo.enterprise.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;

public class PersonEncryptionSchema {


//	public static void main(String[] args) throws IOException {
//
//		String schema = personSchema("5e986a3c-74b5-463a-866c-a0f44fb8bb2d").toDocument().toJson().toString(); // replace "your_key_id" with your base64 data encryption key id
//
//		String path = "fle-local-schema.json";
//		try (FileOutputStream stream = new FileOutputStream(path)) {
//			stream.write(schema.getBytes());
//		}
//
//		System.out.println(schema);
//	}
//
//	private static MongoJsonSchema personSchema(String keyId){
//
//		UUID keyUUID = UUID.fromString(keyId);
//
//
//		MongoJsonSchema mongoJsonSchema = MongoJsonSchema.builder()
//
//				//.required("firstName","lastName")
//				.properties(
//						encrypted(string("ssn"))
//						.aead_aes_256_cbc_hmac_sha_512_deterministic()
//						.keys(keyUUID)
//				)
//				.properties(
//						encrypted(string("bloodType"))
//						.aead_aes_256_cbc_hmac_sha_512_random()
//								.keys(keyUUID)
//
//				)
//				.properties(
//						encrypted(string("phone"))
//						.aead_aes_256_cbc_hmac_sha_512_random()
//								.keys(keyUUID)
//
//
//				)
//				.build();
//		System.out.println(mongoJsonSchema.toDocument().toJson());
//		return mongoJsonSchema;
//	}


//
//	private static Document getKeyID(String keyId) {
//		List<> keyIds = new ArrayList<>();
//		keyIds.add(new Document()
//				.append("$binary", new Document()
//						.append("base64", keyId)
//						.append("subType", "04")));
//		return new Document().append("keyId", keyIds);
//	}

//
//	public static Document getDocument(String keyId){
//		return ((Document) personSchema(keyId).toDocument()
//				.get("$jsonSchema")
//		).append("encryptMetadata", createEncryptMetadataSchema(keyId))
//				;
//	}

	public static Document getDocument(String keyId){

		Document document = new Document();
		document.append("bsonType", "object")
				//.append("encryptMetadata", createEncryptMetadataSchema(keyId))
			//.append("required", Arrays.asList(new String[]{"firstName","lastName"}))
				.append("properties", new Document()
					.append("ssn",buildEncryptedField("int", true, keyId))
					.append("bloodType",buildEncryptedField("string", false, keyId))
					.append("phone",buildEncryptedField("string", false,keyId))
				);

		return document;
	}

	// JSON Schema helpers
	private static Document buildEncryptedField(String bsonType, Boolean isDeterministic,String keyId) {
		String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
		String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";

		return new Document().
				append("encrypt", new Document()
						.append("bsonType", bsonType)
						.append("keyId", Arrays.asList(new Document()
								.append("$binary", new Document()
										.append("base64", keyId)
										.append("subType", "04"))))
						.append("algorithm",
								(isDeterministic) ? DETERMINISTIC_ENCRYPTION_TYPE : RANDOM_ENCRYPTION_TYPE));
	}


	private static Document createEncryptMetadataSchema(String keyId) {
		List<Document> keyIds = new ArrayList<>();
		keyIds.add(new Document()
				.append("$binary", new Document()
						.append("base64", keyId)
						.append("subType", "04")));
		return new Document().append("keyId", keyIds);
	}

}

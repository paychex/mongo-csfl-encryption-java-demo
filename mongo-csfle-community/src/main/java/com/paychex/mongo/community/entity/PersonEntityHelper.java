
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

package com.paychex.mongo.community.entity;

import com.mongodb.client.model.vault.EncryptOptions;
import com.paychex.mongo.community.keymangement.KMSHandler;
import org.bson.BsonBinary;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
	Author: Visweshwar Ganesh
	created on 1/21/20 10:07 AM
*/
@Component
public class PersonEntityHelper {

	@Autowired
	protected KMSHandler kmsHandler;

	public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
	public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";


	public EncryptedPerson getEncrypedPerson(Person p) {

		EncryptedPerson ep = new EncryptedPerson(p.getFirstName(),p.getLastName());
		ep.setSsn(kmsHandler.getClientEncryption().encrypt(new BsonInt32(p.getSsn()),getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE)));
		ep.setPhone(kmsHandler.getClientEncryption().encrypt(new BsonString(p.getPhone()),getEncryptOptions(RANDOM_ENCRYPTION_TYPE)));
		ep.setBloodType(kmsHandler.getClientEncryption().encrypt(new BsonString(p.getBloodType()),getEncryptOptions(RANDOM_ENCRYPTION_TYPE)));
		return ep;
	}

	public Person getPerson(EncryptedPerson ep){

		Person p = new Person(ep.getFirstName(),ep.getLastName());
		p.setSsn(kmsHandler.getClientEncryption().decrypt(ep.getSsn()).asNumber().intValue());
		p.setPhone(kmsHandler.getClientEncryption().decrypt(ep.getPhone()).asString().getValue());
		p.setBloodType(kmsHandler.getClientEncryption().decrypt(ep.getBloodType()).asString().getValue());
		return p;

	}

	public BsonBinary getEncryptedSsn(int ssn){
		return kmsHandler.getClientEncryption().encrypt(new BsonInt32(ssn),getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE));
	}

	private  EncryptOptions getEncryptOptions(String algorithm){

		EncryptOptions encryptOptions = new EncryptOptions(algorithm);
		encryptOptions.keyId(new BsonBinary(kmsHandler.getEncryptionKeyUUID()));
		return encryptOptions;

	}

}

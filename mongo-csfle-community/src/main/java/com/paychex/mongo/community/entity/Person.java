
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

import org.springframework.data.annotation.Id;

/*
	Author: Visweshwar Ganesh
	created on 1/21/20
*/
public class Person {


	@Id
	private String id;

	private String firstName;
	private String lastName;
	private int ssn;
	private String phone;
	private String bloodType;

	public Person() {}

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Person( String firstName, String lastName, int ssn, String phone, String bloodType) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.ssn = ssn;
		this.phone = phone;
		this.bloodType = bloodType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getSsn() {
		return ssn;
	}

	public void setSsn(int ssn) {
		this.ssn = ssn;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	@Override
	public String toString() {
		return "Person{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + '}';
	}
}

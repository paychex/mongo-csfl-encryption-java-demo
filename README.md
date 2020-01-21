# mongo-csfl-encryption-java-demo
This project demonstrates Client Side Field Level Encryption(CSFLE) using Java Spring for Enterprise and Community edition of MongoDB

Applications can encrypt fields in documents prior to transmitting data over the wire to the server. Only applications with access to the correct encryption keys can decrypt and read the protected data. Deleting an encryption key renders all data encrypted using that key as permanently unreadable.

## How To Implement CSFLE



# Minimum Requirements

To leverage Client side field level Encryption we need
* [Mongo 4.2+](https://www.mongodb.com/download-center/community)
* Java Mongo Driver 3.11+
* [libmongocrypt 1.0+](https://github.com/mongodb/libmongocrypt) The companion C library for client side encryption in drivers. Follow instructions based on the OS of your choice.
* [mongocryptd](https://docs.mongodb.com/manual/reference/security-client-side-encryption-appendix/#mongocryptd) **\*** *Enterprise Edition Only*
  * This can be provided as a part of the installation or standalone. 
  * More on this will be available in the enterprise project ReadMe.  

Other Requirements will be driven by the framework of choice

# Community Edition Vs Enterprise Edition

CSFLE is offered on both Community and Enterprise Edition. The only difference is that Enterprise edition can perform automatic encryption based on JSONSchema.

For the community edition the application will be responsible for performing this encryption.

Based on your preference and organization policies you can use the appropriate project to leverage CSFLE.

# Disclaimer
The project is not to for Production use. This is a demonstration of CSFLE functionality.
Continue to review latest bug fixes and updates on the Mongo and Java Spring to keep your code up to date.
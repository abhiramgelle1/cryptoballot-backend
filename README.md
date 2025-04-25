# CryptoBallot Backend

A Spring Boot backend for CryptoBallot — a secure electronic voting system using **Paillier encryption** and **RSA blind signatures**. This backend handles user authentication, vote encryption, aggregation, result decryption, and simulated attack resistance.

---

## Table of Contents

1. [Features](#features)  
2. [Technology Stack](#technology-stack)  
3. [Prerequisites](#prerequisites)  
4. [Database Setup](#database-setup)  
5. [Configuration](#configuration)  
6. [Build & Run](#build--run)  
7. [API Endpoints](#api-endpoints)  
8. [Database Schema](#database-schema)  
9. [Security Mechanisms](#security-mechanisms)  
10. [Attack Simulation Features](#attack-simulation-features)  
11. [Tally & Verification](#tally--verification)  

---

## Features

- BCrypt-based voter registration & login  
- Paillier homomorphic encryption for secure vote casting  
- RSA blind signatures for voter anonymity and authentication  
- Vote storage with nonce protection against replay attacks  
- Homomorphic vote aggregation and result decryption  
- Live simulation of replay, substitution, and injection attacks  

---

## Technology Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **JPA / Hibernate**
- **PostgreSQL**
- **Paillier cryptosystem (custom implementation)**
- **RSA blind signature logic**
- **Maven**

---

## Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL 12+

---

## Database Setup

```
CREATE DATABASE cryptoballotdb;

CREATE USER ballot_user WITH ENCRYPTED PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE cryptoballotdb TO ballot_user;
```

Db Connection

- Need to connect with postgresql where already code configured.
- Just need to add db with name `cryptoballotdb` in default postgres port 

![Screenshot 2025-04-22 104041](https://github.com/user-attachments/assets/21f40114-779c-4b23-8e26-a688769fc5a1)
![Screenshot 2025-04-22 104101](https://github.com/user-attachments/assets/f3090798-8e97-47a7-bd6e-12f00baf854e)

## Configuration
Edit the file: src/main/resources/application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/cryptoballotdb
spring.datasource.username=<your_user>
spring.datasource.password=<your_password>
spring.jpa.hibernate.ddl-auto=update
server.port=8080

## Build & Run

- git clone https://github.com/your-org/cryptoballot-backend.git
- cd cryptoballot-backend

- mvn clean package
- mvn spring-boot:run

```
Backend available at: http://localhost:8080
```

## API Endpoints
- Authentication Endpoints
![image](https://github.com/user-attachments/assets/532a0ce5-4557-4fc6-a567-eb6123cca4ba)

- Encryption Endpoints
![image](https://github.com/user-attachments/assets/b96f8de0-8884-4980-8efd-a116c3ea7ccc)

- Casting Vote with RSA blind signature Endpoints
![image](https://github.com/user-attachments/assets/e5b269a2-efc4-4d58-bcf3-be77270c5cd5)

- Voting Management Endpoints
![image](https://github.com/user-attachments/assets/a5739586-f351-4449-908d-29884f473f8b)

- Attack Endpoints
![image](https://github.com/user-attachments/assets/4eeb01b5-665d-46f5-9cad-28509e2abaf2)


## Database Schema
- Voters Table
![image](https://github.com/user-attachments/assets/2b782ce7-19a2-4068-9651-b1ba0012e5f8)

- Votes Table
![image](https://github.com/user-attachments/assets/c92500c8-fa02-4ca8-87ab-974a7d756655)


## Security Mechanisms

1. Paillier Encryption: Confidentiality + Homomorphic tally
2. RSA Blind Signature: Voter anonymity + verifiable approval
3. BCrypt: Secure password hashing
4. Nonce: Prevents replay attacks during vote casting
5. JWT / Session Auth (if extended): Secure user management


## Attack Simulation Features
- Replay Attack
Detects and blocks duplicate vote submissions using stored nonces.

- Substitution Attack
Alters an existing encrypted vote in the database and shows how decryption fails.

- Injection Attack
Tests SQL injection attempts on vote and user inputs — demonstrates secure parameterization.


## Tally & Verification
1. Uses Paillier homomorphic multiplication to aggregate all encrypted votes.
2. Authority decrypts total votes using their private key.
3. Logged-in users can also view their personal vote breakdown.

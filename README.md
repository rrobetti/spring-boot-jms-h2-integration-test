# Spring Boot JMS Integration Test with ActiveMQ and H2 Database

This project demonstrates how to write integration tests in **Spring Boot** that involve **JMS messaging** with **ActiveMQ** and **H2 Database**. The goal is to test the flow of JMS messages from one queue to another and verify the data stored in an H2 database using JPA.

## Overview

This project involves:
- **JMS messaging**: Testing the sending and receiving of messages in ActiveMQ.
- **ActiveMQ**: The JMS provider running on the local machine to facilitate the message flow.
- **H2 Database**: An in-memory database used for testing purposes to store and verify message data.
- **Spring Boot**: A Java-based framework used to create the JMS application and integration tests.
- **JUnit 5**: The testing framework used to execute the tests.

### Key Components
- **JMS Listener**: Listens for messages in a queue, processes them, and stores the results in an H2 database.
- **JMS Sender**: Sends messages to a destination queue for further processing.
- **Test Flow**:
    - A message is placed on the **source queue**.
    - The **JMS listener** receives the message, processes it, and stores data in the **H2 database**.
    - The processed message is then forwarded to a **destination queue** for further handling.


## Test Flow

### 1. **JMS Message Listener**:
- The **JMS Listener** listens for messages from the **source queue**.
- Upon receiving a message, the listener processes it and stores the data in the **H2 database**.
- The listener then forwards the processed message to the **destination queue**.

### 2. **JMS Sender**:
- The **JMS Sender** is responsible for placing test messages on the **source queue**.
- Once the message is placed, the **JMS Listener** picks it up and processes it.

### 3. **Database Verification**:
- After processing, the test verifies that the data is correctly stored in the **H2 database**.
- The test also checks that the message in the **destination queue** is correct.

### 4. **Test Execution**:
- The integration test starts an embedded **ActiveMQ broker** and verifies the behavior of JMS message sending, receiving, and database storage.

## Prerequisites

Before running the tests, ensure you have the following installed:
- **Java 17+**
- **Maven**: To build the project and run the tests.
- **ActiveMQ**: Running locally or use an embedded broker for the tests.

## Running the Tests

### Running the tests via Maven:
To run the integration tests, use Maven in the project directory:

```bash
mvn clean test



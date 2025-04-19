// src/test/java/com/example/jms/JmsIntegrationTest.java
package com.example.jms;

import org.apache.activemq.broker.BrokerService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
        "source.queue=test.source.queue",
        "destination.queue=test.destination.queue",
        "spring.activemq.user=admin",
        "spring.activemq.password=admin",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
public class JmsIntegrationTest {

    private static final String BROKER_URL = "vm://localhost:61616";
    private static BrokerService broker;

    @DynamicPropertySource
    static void setupProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.activemq.broker-url", () -> BROKER_URL);
    }

    @BeforeAll
    static void startBroker() throws Exception {
        broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.addConnector(BROKER_URL);
        broker.setBrokerName("test-broker-1");  // Use a unique broker name
        broker.setUseShutdownHook(false); //Prevents broker shutdown hooked with JVM shutdown
        broker.start();
    }

    @AfterAll
    static void stopBroker(@Autowired ConfigurableApplicationContext context) throws Exception {
        context.stop();
        broker.stop();
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void testMessageFlow() throws InterruptedException {
        messageRepository.deleteAll();

        String testMessage = "Hello Embedded";
        jmsTemplate.convertAndSend("test.source.queue", testMessage);

        Thread.sleep(100);

        List<MessageEntity> stored = messageRepository.findAll();
        assertEquals(1, stored.size());
        assertEquals(testMessage, stored.get(0).getContent());

        String response = (String) jmsTemplate.receiveAndConvert("test.destination.queue");
        assertNotNull(response);
        assertEquals("Processed: " + testMessage, response);
    }
}

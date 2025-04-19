// src/main/java/com/example/jms/MessageProcessor.java
package com.example.jms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessor {

    private final MessageRepository messageRepository;
    private final JmsTemplate jmsTemplate;

    @Value("${destination.queue}")
    private String destinationQueue;

    public MessageProcessor(MessageRepository messageRepository, JmsTemplate jmsTemplate) {
        this.messageRepository = messageRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "${source.queue}")
    public void receiveMessage(String message) {
        MessageEntity entity = new MessageEntity();
        entity.setContent(message);
        messageRepository.save(entity);
        jmsTemplate.convertAndSend(destinationQueue, "Processed: " + message);
    }
}

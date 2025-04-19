// src/main/java/com/example/jms/MessageRepository.java
package com.example.jms;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}

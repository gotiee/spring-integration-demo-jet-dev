package jetdev.integration.service;

import jetdev.integration.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageProducer {
    private final KafkaTemplate<String, User> kafkaTemplate;

    public void sendMessage(String topic, User user) {
        try {
            kafkaTemplate.send(topic, user.getEmail(), user);

        } catch (Exception e) {
            log.error("Erreur lors de l'envoi du message à Kafka: {}", e.getMessage());
            throw e;
        }
    }
}

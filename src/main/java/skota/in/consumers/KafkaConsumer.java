package skota.in.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(topics = "spring-kafka-topic", groupId = "spring-kafka-group")
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }

}

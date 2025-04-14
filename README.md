# Spring Boot Kafka Integration

This project demonstrates how to integrate Apache Kafka with Spring Boot using Docker containers.

## Prerequisites

- Docker and Docker Compose installed on your system
- Java 17 or higher
- Maven

## Project Structure

```
spring-boot-kafka/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── skota/
│   │   │       └── in/
│   │   │           ├── consumers/
│   │   │           │   └── KafkaConsumer.java
│   │   │           ├── producers/
│   │   │           │   └── KafkaProducer.java
│   │   │           └── SpringBootKafkaApplication.java
│   │   └── resources/
│   │       └── application.properties
├── docker-compose.yml
└── pom.xml
```

## Docker Setup

1. The project uses multiple Docker Compose files for better organization:

- `docker-compose.yml` - Main file that extends other configurations
- `docker-compose-kafka.yml` - Kafka-specific configuration
- `docker-compose-services.yml` - Spring Boot application configuration

2. Start the containers:

```bash
docker-compose up -d
```

This will start:
- Apache Kafka broker (version 4.0.0)
- Kafka UI (accessible at http://localhost:8080)
- Spring Boot application (accessible at http://localhost:8083)

## Application Configuration

1. Update `application.properties`:

```properties
server.port=8083
spring.application.name=spring-boot-kafka
```

## Code Snippets

### Kafka Consumer

```java
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(topics = "spring-kafka-topic", groupId = "spring-kafka-group")
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }
}
```

### Kafka Producer

```java
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        kafkaTemplate.send("spring-kafka-topic", message);
        System.out.println("Message sent: " + message);
    }
}
```

To use the producer in your application, you can inject it into your services or controllers:

```java
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent successfully");
    }
}
```

## Running the Application

1. Build the project:

```bash
mvn clean install
```

2. Run the Spring Boot application:

```bash
mvn spring-boot:run
```

## Testing the Integration

1. Access Kafka UI at http://localhost:8080 to monitor topics and messages

2. Create a Kafka topic using Kafka UI or via command line or using NewTopic:

```bash
docker exec -it spring-boot-kafka_kafka_1 kafka-topics --create --topic spring-kafka-topic --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1
```

3. Send messages to the topic using Kafka UI or via command line:

```bash
docker exec -it spring-boot-kafka_kafka_1 kafka-console-producer --topic spring-kafka-topic --bootstrap-server kafka:9092
```

4. Type your messages and press Enter. You should see the messages being consumed by your Spring Boot application.

## Dependencies

The project uses the following key dependencies:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## Monitoring

Access Kafka UI at http://localhost:8080 to:
- View topics and their configurations
- Monitor message flow
- View consumer groups
- Check broker status
- Create and manage topics

## Troubleshooting

1. If the consumer is not receiving messages:
   - Check if Kafka container is running: `docker ps`
   - Verify the topic exists in Kafka UI
   - Check application logs for any errors
   - Ensure the Spring Boot application can connect to Kafka (check network connectivity)
   - Verify the Kafka bootstrap server address is correct (kafka:29092)

2. If you need to restart the containers:
   ```bash
   docker-compose down
   docker-compose up -d
   ```

3. If Kafka UI is not accessible:
   - Check if the container is running: `docker ps`
   - Verify port 8080 is not in use
   - Check container logs: `docker logs kafka-ui`

## Cleanup

To stop and remove all containers and volumes:

```bash
docker-compose down -v
``` 
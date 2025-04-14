package skota.in.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import skota.in.producers.KafkaProducer;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/message")
    public String sendMessage(@RequestBody String message) {
        kafkaProducer.sendMessage(message);
        return "Message sent successfully";
    }



}

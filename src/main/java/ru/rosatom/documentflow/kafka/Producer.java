package ru.rosatom.documentflow.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class Producer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> future =
                kafkaTemplate.send("documentFinalStatus", message).completable();
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.trace("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                log.trace("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
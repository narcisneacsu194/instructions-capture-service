package com.example.instructions.service;

import com.example.instructions.model.PlatformTrade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaPublisher {

    private final KafkaTemplate<String, String> template;
    private final ObjectMapper objectMapper;

    @Value("${topics.outbound}")
    private String outboundTopic;

    public void publish(PlatformTrade trade) {
        try {
            String json = objectMapper.writeValueAsString(trade);
            template.send(outboundTopic, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing PlatformTrade", e);
        }
    }
}

package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import com.example.instructions.util.TradeTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final KafkaPublisher kafkaPublisher;
    private final ObjectMapper objectMapper;

    // in-memory storage for auditing / retry
    private final Map<String, CanonicalTrade> inMemoryStore = new ConcurrentHashMap<>();

    public CanonicalTrade fromJson(String json) {
        try {
            return objectMapper.readValue(json, CanonicalTrade.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JSON trade payload", e);
        }
    }

    public void processTrade(CanonicalTrade trade) {
        CanonicalTrade normalized = TradeTransformer.normalize(trade);
        PlatformTrade platformTrade = TradeTransformer.toPlatformJson(normalized);

        // store using a random id – avoids leaking account/security directly as key
        inMemoryStore.put(UUID.randomUUID().toString(), normalized);

        kafkaPublisher.publish(platformTrade);
    }
}

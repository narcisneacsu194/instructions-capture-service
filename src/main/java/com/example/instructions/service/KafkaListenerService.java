package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaListenerService {

    private final TradeService tradeService;

    @KafkaListener(topics = "${topics.inbound}")
    public void consume(String message) {
        CanonicalTrade trade = tradeService.fromJson(message);
        tradeService.processTrade(trade);
    }
}

package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TradeServiceTest {

    @Test
    void processTradePublishesToKafka() {
        KafkaPublisher publisher = mock(KafkaPublisher.class);
        TradeService service = new TradeService(publisher, new ObjectMapper());

        CanonicalTrade t = new CanonicalTrade();
        t.setAccountNumber("ACC123456789");
        t.setSecurityId("abc123");
        t.setTradeType("Buy");
        t.setAmount(100000);
        t.setTimestamp("2025-08-04T21:15:33Z");

        service.processTrade(t);

        ArgumentCaptor<com.example.instructions.model.PlatformTrade> captor =
                ArgumentCaptor.forClass(com.example.instructions.model.PlatformTrade.class);

        verify(publisher, times(1)).publish(captor.capture());
        assertThat(captor.getValue().getTrade().getAccount()).isEqualTo("*****6789");
    }
}

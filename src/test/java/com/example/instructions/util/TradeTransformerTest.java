package com.example.instructions.util;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TradeTransformerTest {

    @Test
    void normalizeShouldMaskAccountAndUppercaseAndMapType() {
        CanonicalTrade t = new CanonicalTrade();
        t.setAccountNumber("ACC123456789");
        t.setSecurityId("abc123");
        t.setTradeType("buy");
        t.setAmount(100000);
        t.setTimestamp("2025-08-04T21:15:33Z");

        CanonicalTrade n = TradeTransformer.normalize(t);

        assertThat(n.getAccountNumber()).isEqualTo("*****6789");
        assertThat(n.getSecurityId()).isEqualTo("ABC123");
        assertThat(n.getTradeType()).isEqualTo("B");
    }

    @Test
    void toPlatformJsonShouldMapFields() {
        CanonicalTrade t = new CanonicalTrade();
        t.setAccountNumber("*****6789");
        t.setSecurityId("ABC123");
        t.setTradeType("B");
        t.setAmount(100000);
        t.setTimestamp("2025-08-04T21:15:33Z");

        PlatformTrade p = TradeTransformer.toPlatformJson(t);

        assertThat(p.getTrade().getAccount()).isEqualTo("*****6789");
        assertThat(p.getTrade().getSecurity()).isEqualTo("ABC123");
        assertThat(p.getTrade().getType()).isEqualTo("B");
    }
}

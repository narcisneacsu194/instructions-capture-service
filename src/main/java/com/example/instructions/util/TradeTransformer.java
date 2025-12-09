package com.example.instructions.util;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;

public class TradeTransformer {

    public static CanonicalTrade normalize(CanonicalTrade t) {

        if (t.getAccountNumber() != null && t.getAccountNumber().length() >= 4) {
            String acc = t.getAccountNumber().replaceAll("\\s+", "");
            String last4 = acc.substring(acc.length() - 4);
            t.setAccountNumber("*****" + last4);
        }

        if (t.getSecurityId() != null) {
            t.setSecurityId(t.getSecurityId().trim().toUpperCase());
        }

        if (t.getTradeType() != null) {
            String type = t.getTradeType().trim().toUpperCase();
            t.setTradeType(type.startsWith("B") ? "B" : "S");
        }

        return t;
    }

    public static PlatformTrade toPlatformJson(CanonicalTrade t) {
        return PlatformTrade.builder()
                .platformId("ACCT123")
                .trade(PlatformTrade.TradeDetails.builder()
                        .account(t.getAccountNumber())
                        .security(t.getSecurityId())
                        .type(t.getTradeType())
                        .amount(t.getAmount())
                        .timestamp(t.getTimestamp())
                        .build())
                .build();
    }
}

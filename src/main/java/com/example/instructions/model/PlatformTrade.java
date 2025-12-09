package com.example.instructions.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlatformTrade {
    private String platformId;
    private TradeDetails trade;

    @Data
    @Builder
    public static class TradeDetails {
        private String account;
        private String security;
        private String type;
        private double amount;
        private String timestamp;
    }
}

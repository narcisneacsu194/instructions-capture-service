package com.example.instructions.model;

import lombok.Data;

@Data
public class CanonicalTrade {
    private String accountNumber;
    private String securityId;
    private String tradeType; // BUY / SELL normalized
    private double amount;
    private String timestamp;
}

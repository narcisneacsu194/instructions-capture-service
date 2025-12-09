package com.example.instructions.controller;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.service.TradeService;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;

@RestController
@RequestMapping("/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String uploadJson(@RequestBody CanonicalTrade trade) {
        tradeService.processTrade(trade);
        return "Processed JSON trade";
    }

    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            // assume header row: accountNumber,securityId,tradeType,amount,timestamp
            boolean first = true;
            while ((line = reader.readNext()) != null) {
                if (first) { // skip header
                    first = false;
                    continue;
                }
                if (line.length < 5) continue; // simple validation

                CanonicalTrade trade = new CanonicalTrade();
                trade.setAccountNumber(line[0]);
                trade.setSecurityId(line[1]);
                trade.setTradeType(line[2]);
                trade.setAmount(Double.parseDouble(line[3]));
                trade.setTimestamp(line[4]);

                tradeService.processTrade(trade);
            }
            return "Processed CSV file";
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }
}

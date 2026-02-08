package com.electrumprep.ledger.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@Service
public class CategorizationService {

    // 1. Professional Logging (Replaces e.printStackTrace)
    private static final Logger logger = LoggerFactory.getLogger(CategorizationService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String categorize(String reference, String amount) {
        // 2. Fix: Use the apiUrl variable properly
        String fullUrl = apiUrl + apiKey;

        String prompt = "You are a banking AI. Classify this transaction: '" + reference + "' (Amount: " + amount + "). " +
                "Strictly choose ONE category from this exact list: " +
                "[Food & Dining, Groceries, Transport, Entertainment, Shopping, Utilities, Health, Tech, Transfer]. " +
                "Reply ONLY with the category name. Do not explain.";

        // 3. Fix: Extracted JSON building to a clean helper method
        Map<String, Object> requestBody = createRequestBody(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 4. Fix: Use JsonNode instead of Raw Maps (Eliminates type warnings)
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(fullUrl, entity, JsonNode.class);
            JsonNode root = response.getBody();

            if (root != null && root.has("candidates") && !root.path("candidates").isEmpty()) {
                // Safely traverse the JSON tree
                return root.path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText()
                        .trim()
                        .replace("\n", "");
            }
            return "Unknown (No Data)";

        } catch (Exception e) {
            // 5. Secure Error Logging
            logger.error("AI Service Error: {}", e.getMessage());
            return "Unknown (AI Error)";
        }
    }

    // Helper Method to keep the main logic clean
    private Map<String, Object> createRequestBody(String prompt) {
        return Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );
    }
}
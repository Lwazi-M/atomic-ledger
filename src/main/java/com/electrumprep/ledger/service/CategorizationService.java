package com.electrumprep.ledger.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.List;

@Service
public class CategorizationService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String categorize(String reference, String amount) {
        String fullUrl = apiUrl + apiKey;

        // The Question we ask the AI
        String prompt = "Categorize this bank transaction: '" + reference + "' for amount " + amount + ". " +
                "Return ONLY one word from this list: [Groceries, Utilities, Entertainment, Transport, Salary, Unknown]. " +
                "Do not write a sentence.";

        // JSON Structure for Gemini API
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Send to Google
            ResponseEntity<Map> response = restTemplate.postForEntity(fullUrl, entity, Map.class);

            // Extract the answer (Parsing the complex JSON response)
            Map body = response.getBody();
            List candidates = (List) body.get("candidates");
            Map firstCandidate = (Map) candidates.get(0);
            Map content = (Map) firstCandidate.get("content");
            List parts = (List) content.get("parts");
            Map firstPart = (Map) parts.get(0);

            return ((String) firstPart.get("text")).trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown (AI Error)";
        }
    }
}
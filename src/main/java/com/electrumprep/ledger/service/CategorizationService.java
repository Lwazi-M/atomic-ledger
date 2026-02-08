package com.electrumprep.ledger.service;

// 📦 IMPORTS
// We are grabbing tools from the Java library shelf.
// "JsonNode" helps us handle the complex data format (JSON) that Google sends back.
// "Logger" helps us write messages to the console instead of just crashing.
// "Cacheable" is the memory tool that makes our app fast.
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

// -----------------------------------------------------------------------------------
// 🤖 THE AI SERVICE (The Translator)
// This class is responsible for talking to Google's "Gemini" Brain.
// It takes messy transaction text (e.g., "Uber * 8721") and asks Google to
// turn it into a clean category (e.g., "Transport").
// -----------------------------------------------------------------------------------

@Service // 🏷️ STICKER: Tells Spring Boot: "This is a Worker Class. Keep it ready to do jobs."
public class CategorizationService {

    // 📜 THE CAPTAIN'S LOG
    // Instead of printing random stuff to the screen, we use a Logger.
    // It records important events (like errors) so we can fix them later.
    private static final Logger logger = LoggerFactory.getLogger(CategorizationService.class);

    // 🔑 SECRETS FROM THE SAFE
    // We never type passwords directly in the code!
    // @Value grabs the API Key from the safe (Environment Variables) and injects it here.
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    // 📞 THE PHONE
    // RestTemplate is the tool we use to "call" other computers (like Google).
    private final RestTemplate restTemplate = new RestTemplate();

    // ===================================================================================
    // 🧠 THE BRAIN METHOD
    // This is the function that actually does the thinking.
    // ===================================================================================

    // 📌 THE STICKY NOTE TRICK (@Cacheable)
    // Before running this heavy code, Spring Boot checks its memory (Cache).
    // It asks: "Have I categorized 'KFC' + '120.00' before?"
    // - IF YES: It returns the saved answer instantly (0 seconds). ⚡
    // - IF NO: It runs the code below, asks Google, then saves the answer on a sticky note.
    @Cacheable("categories")
    public String categorize(String reference, String amount) {

        // 1. Prepare the Phone Number (URL)
        // We combine the Google Address + Your Secret Key to get the full link.
        String fullUrl = apiUrl + apiKey;

        // 2. Write the Instructions (The Prompt)
        // We act like a strict boss telling an intern what to do.
        // We give it a specific "Menu" of options so it doesn't invent random words.
        String prompt = "You are a banking AI. Classify this transaction: '" + reference + "' (Amount: " + amount + "). " +
                "Strictly choose ONE category from this exact list: " +
                "[Food & Dining, Groceries, Transport, Entertainment, Shopping, Utilities, Health, Tech, Transfer]. " +
                "Reply ONLY with the category name. Do not explain.";

        // 3. Package the Letter (JSON)
        // Computers speak in JSON format (curly braces {}).
        // We use a helper method below to pack our prompt into a neat box.
        Map<String, Object> requestBody = createRequestBody(prompt);

        // 4. Stamp the Envelope (Headers)
        // We tell Google: "I am sending you JSON data, please read it correctly."
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 5. Combine Letter + Envelope (Entity)
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 6. SEND THE REQUEST! (Making the Call)
            // This is where we actually talk to Google.
            // We wait here for about 1 second for Google to reply.
            // "JsonNode.class" means: "Treat the reply as a flexible data tree."
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(fullUrl, entity, JsonNode.class);
            JsonNode root = response.getBody();

            // 7. Open the Reply (Peeling the Onion)
            // Google wraps the answer in many layers of folders.
            // We have to dig deep to find the actual text we want.
            // Structure: Response -> Candidates List -> First Candidate -> Content -> Parts -> Text
            if (root != null && root.has("candidates") && !root.path("candidates").isEmpty()) {
                return root.path("candidates")
                        .get(0)                 // Get the first answer
                        .path("content")        // Open the content box
                        .path("parts")          // Look at the parts
                        .get(0)                 // Get the first part
                        .path("text")           // READ THE TEXT! (e.g., "Food & Dining")
                        .asText()
                        .trim()                 // Cut off accidental spaces
                        .replace("\n", ""); // Cut off accidental new lines
            }
            return "Unknown (No Data)";

        } catch (Exception e) {
            // 8. SAFETY NET (Error Handling)
            // If the internet breaks, or Google is down, or we run out of credit...
            // Do NOT crash the app.
            // Just log the error in the captain's log and return a safe default value.
            logger.error("AI Service Error: {}", e.getMessage());
            return "Unknown (AI Error)";
        }
    }

    // 🧹 HELPER METHOD
    // This just keeps the main code clean.
    // It builds the complex nested Map structure that Google requires.
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
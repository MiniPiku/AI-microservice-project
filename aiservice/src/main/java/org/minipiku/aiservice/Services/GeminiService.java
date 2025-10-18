package org.minipiku.aiservice.Services;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String getRecommendations(String details) {
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", details)
                        })
                }
        );

        JsonNode response = webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // ✅ Safely extract text from Gemini’s response
        if (response != null &&
                response.has("candidates") &&
                response.get("candidates").get(0).has("content") &&
                response.get("candidates").get(0).get("content").has("parts")) {

            return response.get("candidates")
                    .get(0)
                    .get("content")
                    .get("parts")
                    .get(0)
                    .get("text")
                    .asText()
                    .replaceAll("```json","")
                    .replaceAll("```","");
        }

        return "No response from Gemini.";
    }
}

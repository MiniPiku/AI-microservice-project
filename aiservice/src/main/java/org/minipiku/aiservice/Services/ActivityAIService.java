package org.minipiku.aiservice.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.minipiku.aiservice.Interface.RecommendationRepository;
import org.minipiku.aiservice.Model.Activity;
import org.minipiku.aiservice.Model.Recommendation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;
    private final RecommendationRepository recommendationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void generateRecommendation(Activity activity) {
        try {
            String prompt = createPromptForActivity(activity);
            String response = geminiService.getRecommendations(prompt);

            log.info("AI raw response for user {}: {}", activity.getUserId(), response);

            JsonNode jsonNode = objectMapper.readTree(response);

            String formattedAnalysis = String.format(
                    "overall: %s, \n" +
                            "pace: %s, \n" +
                            "heartRate: %s, \n" +
                            "caloriesBurned: %s",
                    jsonNode.path("analysis").path("overall").asText(""),
                    jsonNode.path("analysis").path("pace").asText(""),
                    jsonNode.path("analysis").path("heartRate").asText(""),
                    jsonNode.path("analysis").path("caloriesBurned").asText("")
            );

            Recommendation recommendation = Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .recommendation(formattedAnalysis)  // change type from Map<String,String> to String
                    .improvements(extractFieldList(jsonNode.path("improvements"), "recommendation"))
                    .suggestions(extractFieldList(jsonNode.path("suggestions"), "description"))
                    .safety(extractSafetyList(jsonNode.path("safety")))
                    .createdAt(LocalDateTime.now())
                    .build();


            recommendationRepository.save(recommendation);

            log.info("Saved AI recommendation for user {} to MongoDB", activity.getUserId());

        } catch (Exception e) {
            log.error("Error generating recommendation for user {}: {}", activity.getUserId(), e.getMessage(), e);
        }
    }

    private List<String> extractFieldList(JsonNode node, String fieldName) {
        List<String> list = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode n : node) {
                String val = n.path(fieldName).asText(null);
                if (val != null && !val.isEmpty()) list.add(val);
            }
        }
        return list;
    }

    private List<String> extractSafetyList(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode n : node) {
                if (n.isTextual()) list.add(n.asText());
                else if (n.isObject()) {
                    n.fieldNames().forEachRemaining(key -> list.add(key));
                }
            }
        }
        return list;
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
        {
          "instruction": "Analyze the activity and generate structured fitness insights. Vary tone randomly. Output must match the responseFormat.",
          "activity": {
            "type": "%s",
            "durationMinutes": %s,
            "caloriesBurned": %s,
            "startTime": "%s",
            "additionalMetrics": %s
          },
          "responseFormat": {
            "analysis": {
              "overall": "Summary of performance across all metrics.",
              "pace": "Insight into speed consistency and efficiency.",
              "heartRate": "Evaluation of heart rate zones and intensity.",
              "caloriesBurned": "Assessment of energy expenditure."
            },
            "improvements": [
              {
                "area": "Targeted improvement area (e.g., hydration, recovery)",
                "recommendation": "Specific advice to enhance performance or safety."
              }
            ],
            "suggestions": [
              {
                "workout": "Recommended workout type or variation.",
                "description": "Brief explanation of the workout's purpose or benefit."
              }
            ],
            "safety": [
                "Array of plain strings with safety tips."
            ],
          }
        }
        """,
                activity.getType(),
                activity.getDuration() != null ? activity.getDuration() : "null",
                activity.getCaloriesBurned() != null ? activity.getCaloriesBurned() : "null",
                activity.getStartTime() != null ? activity.getStartTime() : "null",
                activity.getAdditionalMetrics() != null ? activity.getAdditionalMetrics() : "{}",
                activity.getType() // repeated for summary.activityType
        );
    }
}

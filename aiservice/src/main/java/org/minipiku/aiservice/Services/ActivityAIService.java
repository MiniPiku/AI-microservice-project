package org.minipiku.aiservice.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.minipiku.aiservice.Model.Activity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public void generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);

        String recommendation = geminiService.getRecommendations(prompt);

        log.info("AI Recommendation for user {}: {}", activity.getUserId(), recommendation);
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
              },
              {
                "safety": [
                  "Safety tip 1 (e.g., warm-up advice)",
                  "Safety tip 2 (e.g., terrain caution)"
                ]
              }
            ],
            "summary": {
              "activityType": "%s",
              "recommendation": "Concise, practical advice tailored to the activity.",
              "tone": "One of ['motivational', 'technical', 'casual', 'friendly']",
              "tags": ["keyword1", "keyword2", "keyword3"]
            }
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

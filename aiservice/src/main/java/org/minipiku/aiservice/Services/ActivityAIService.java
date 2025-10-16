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
              "activity": {
                "type": "%s",
                "durationMinutes": %s,
                "caloriesBurned": %s,
                "startTime": "%s",
                "additionalMetrics": %s
              },
              "instruction": "Please give short and practical fitness advice or recommendations."
            }
            """,
                activity.getType(),
                activity.getDuration() != null ? activity.getDuration() : "null",
                activity.getCaloriesBurned() != null ? activity.getCaloriesBurned() : "null",
                activity.getStartTime() != null ? activity.getStartTime() : "null",
                activity.getAdditionalMetrics() != null ? activity.getAdditionalMetrics() : "{}"
        );
    }
}
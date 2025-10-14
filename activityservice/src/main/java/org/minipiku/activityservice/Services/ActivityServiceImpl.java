package org.minipiku.activityservice.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.minipiku.activityservice.DTOs.ActivityRequest;
import org.minipiku.activityservice.DTOs.ActivityResponse;
import org.minipiku.activityservice.Models.Activity;
import org.minipiku.activityservice.Models.ActivityType;
import org.minipiku.activityservice.Repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    @Override
    public ActivityResponse trackActivity(ActivityRequest request) {

        // ✅ Validate user before saving activity
        boolean userExists = userValidationService.validateUser(request.getUserId());
        if (!userExists) {
            throw new RuntimeException("Invalid user ID: " + request.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(ActivityType.valueOf(request.getType().toUpperCase()))
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("📝 Saving activity for user {}", request.getUserId());
        Activity saved = activityRepository.save(activity);
        log.info("✅ Saved activity with ID {}", saved.getId());


        //Publish event to Kafka
        kafkaTemplate.send(topicName, saved);

        return ActivityResponse.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .type(saved.getType().name())
                .duration(saved.getDuration())
                .caloriesBurned(saved.getCaloriesBurned())
                .startTime(saved.getStartTime())
                .additionalMetrics(saved.getAdditionalMetrics())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
}


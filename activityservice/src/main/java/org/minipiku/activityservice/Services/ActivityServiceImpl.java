package org.minipiku.activityservice.Services;

import lombok.RequiredArgsConstructor;
import org.minipiku.activityservice.DTOs.ActivityRequest;
import org.minipiku.activityservice.DTOs.ActivityResponse;
import org.minipiku.activityservice.Models.Activity;
import org.minipiku.activityservice.Models.ActivityType;
import org.minipiku.activityservice.Repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;

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

        Activity saved = activityRepository.save(activity);

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


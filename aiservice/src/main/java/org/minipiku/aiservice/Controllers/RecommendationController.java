package org.minipiku.aiservice.Controllers;

import lombok.RequiredArgsConstructor;
import org.minipiku.aiservice.DTOs.RecommendationRequest;
import org.minipiku.aiservice.DTOs.RecommendationResponse;
import org.minipiku.aiservice.Services.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<RecommendationResponse> createRecommendation(
            @RequestBody RecommendationRequest request) {
        return ResponseEntity.ok(recommendationService.createRecommendation(request));
    }

    @GetMapping
    public ResponseEntity<List<RecommendationResponse>> getAllRecommendations() {
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationResponse>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(recommendationService.getRecommendationsByUser(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<RecommendationResponse>> getByActivity(@PathVariable String activityId) {
        return ResponseEntity.ok(recommendationService.getRecommendationsByActivity(activityId));
    }
}

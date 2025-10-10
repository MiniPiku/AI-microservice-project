package org.minipiku.aiservice.Services;

import org.minipiku.aiservice.DTOs.RecommendationRequest;
import org.minipiku.aiservice.DTOs.RecommendationResponse;

import java.util.List;

public interface RecommendationService {
    RecommendationResponse createRecommendation(RecommendationRequest request);
    List<RecommendationResponse> getAllRecommendations();
    List<RecommendationResponse> getRecommendationsByUser(String userId);
    List<RecommendationResponse> getRecommendationsByActivity(String activityId);
}

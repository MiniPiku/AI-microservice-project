package org.minipiku.aiservice.Services;

import lombok.RequiredArgsConstructor;
import org.minipiku.aiservice.DTOs.RecommendationRequest;
import org.minipiku.aiservice.DTOs.RecommendationResponse;
import org.minipiku.aiservice.Interface.RecommendationRepository;
import org.minipiku.aiservice.Model.Recommendation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Override
    public RecommendationResponse createRecommendation(RecommendationRequest request) {
        Recommendation recommendation = Recommendation.builder()
                .activityId(request.getActivityId())
                .userId(request.getUserId())
                .recommendation(request.getRecommendation())
                .improvements(request.getImprovements())
                .suggestions(request.getSuggestions())
                .safety(request.getSafety())
                .createdAt(LocalDateTime.now())
                .build();

        Recommendation saved = recommendationRepository.save(recommendation);
        return mapToResponse(saved);
    }

    @Override
    public List<RecommendationResponse> getAllRecommendations() {
        return recommendationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecommendationResponse> getRecommendationsByUser(String userId) {
        return recommendationRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecommendationResponse> getRecommendationsByActivity(String activityId) {
        return recommendationRepository.findByActivityId(activityId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RecommendationResponse mapToResponse(Recommendation rec) {
        return RecommendationResponse.builder()
                .id(rec.getId())
                .activityId(rec.getActivityId())
                .userId(rec.getUserId())
                .recommendation(rec.getRecommendation())
                .improvements(rec.getImprovements())
                .suggestions(rec.getSuggestions())
                .safety(rec.getSafety())
                .createdAt(rec.getCreatedAt())
                .build();
    }
}


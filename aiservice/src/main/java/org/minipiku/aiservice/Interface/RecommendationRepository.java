package org.minipiku.aiservice.Interface;

import org.minipiku.aiservice.Model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecommendationRepository extends MongoRepository<Recommendation, String> {
    List<Recommendation> findByUserId(String userId);
    List<Recommendation> findByActivityId(String activityId);
}

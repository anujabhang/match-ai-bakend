package com.anuj.match_ai_backend.repositories;

import com.anuj.match_ai_backend.entities.matches.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {
}

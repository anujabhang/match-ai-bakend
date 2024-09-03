package com.anuj.match_ai_backend.repositories;

import com.anuj.match_ai_backend.entities.profiles.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, String> {
}

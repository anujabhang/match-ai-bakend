package com.anuj.match_ai_backend.repositories;

import com.anuj.match_ai_backend.entities.profiles.Profile;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProfileRepository extends MongoRepository<Profile, String> {


    @Aggregation(pipeline = {"{$sample: {size:1}}"})
    Profile getRandomProfile();
}

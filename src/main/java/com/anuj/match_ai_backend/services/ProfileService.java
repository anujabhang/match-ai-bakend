package com.anuj.match_ai_backend.services;

import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    ProfileRepository profileRepository;


    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Optional<Profile> findProfileById(String id) {
        return profileRepository.findById(id);
    }
}

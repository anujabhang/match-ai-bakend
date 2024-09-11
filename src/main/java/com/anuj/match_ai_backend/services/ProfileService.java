package com.anuj.match_ai_backend.services;

import com.anuj.match_ai_backend.entities.profiles.Gender;
import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    ProfileRepository profileRepository;


    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    public List<Profile> getAllProfiles(){
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(String profileId){
        return profileRepository.findById(profileId);
    }

    public Profile getRandomProfile(Gender gender){
        return profileRepository.getRandomProfile(gender);
    }
}

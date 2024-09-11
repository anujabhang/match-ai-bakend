package com.anuj.match_ai_backend.controllers;


import com.anuj.match_ai_backend.entities.profiles.Gender;
import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.services.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
public class ProfileController {

    ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

//
//    @GetMapping("/profile")
//    public List<Profile> getAllProfiles(){
//        return profileService.getAllProfiles();
//    }

    @GetMapping("/profile/{profileId}")
    public Profile getAllProfiles(@PathVariable String profileId){
        return profileService.getProfileById(profileId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Profile with the ID " + profileId));
    }

    @GetMapping("/profile/{gender}/random")
    public Profile getRandomProfile(@PathVariable Gender gender){
        return profileService.getRandomProfile(gender);
    }
}

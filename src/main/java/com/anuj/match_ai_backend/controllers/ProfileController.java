package com.anuj.match_ai_backend.controllers;


import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.services.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProfileController {

    ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @GetMapping("/profile")
    public List<Profile> getAllProfiles(){
        return profileService.getAllProfiles();
    }

    @GetMapping("/profile/random")
    public Profile getRandomProfile(){
        return profileService.getRandomProfile();
    }
}

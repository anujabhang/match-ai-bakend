package com.anuj.match_ai_backend.entities.profiles;

public record Profile(
        String id,
        String firstName,
        String lastName,
        int age,
        String ethnicity,
        Gender gender,
        String bio,
        String imageUrl,
        String myersBriggsPersonalityType
) {


}

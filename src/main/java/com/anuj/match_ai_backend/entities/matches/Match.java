package com.anuj.match_ai_backend.entities.matches;

import com.anuj.match_ai_backend.entities.profiles.Profile;

public record Match(String id, Profile profile, String conversationId) {
}

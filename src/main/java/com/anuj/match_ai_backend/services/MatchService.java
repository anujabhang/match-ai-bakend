package com.anuj.match_ai_backend.services;

import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.entities.matches.Match;
import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.repositories.ConversationRepository;
import com.anuj.match_ai_backend.repositories.MatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MatchService {

    ProfileService profileService;
    ConversationRepository conversationRepository;
    MatchRepository matchRepository;

    public MatchService(ProfileService profileService, ConversationRepository conversationRepository, MatchRepository matchRepository) {
        this.profileService = profileService;
        this.conversationRepository = conversationRepository;
        this.matchRepository = matchRepository;
    }

    public Match createNewMatch(String profileId) {

        Profile profile = profileService.findProfileById(profileId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Profile with the ID " + profileId));


//TODO: make sure there are no existing conversations with this profile already
        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                profileId,
                new ArrayList<>()
        );

        Match match = new Match(
                UUID.randomUUID().toString(),
                profile,
                conversation.id()
        );

        conversationRepository.save(conversation);
        matchRepository.save(match);

        return match;
    }

    public List<Match> getAllMatches(){
        return matchRepository.findAll();
    }
}

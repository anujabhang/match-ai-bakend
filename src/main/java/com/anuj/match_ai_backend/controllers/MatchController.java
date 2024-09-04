package com.anuj.match_ai_backend.controllers;


import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.entities.matches.Match;
import com.anuj.match_ai_backend.services.MatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MatchController {

    MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/matches")
    public Match createNewMatch(@RequestBody CreateMatchRequest request) {

        String profileId = request.profileId();

        return matchService.createNewMatch(profileId);

    }

    public record CreateMatchRequest(
            String profileId
    ) {
    }

    @GetMapping("/matches")
    public List<Match> getAllMatches(){
        return matchService.getAllMatches();
    }
}

package com.anuj.match_ai_backend.controllers;


import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.entities.matches.Match;
import com.anuj.match_ai_backend.services.MatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class MatchController {

    MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/matches")
    public Match createNewMatch(@RequestBody CreateMatchRequest request) {
        String userId = request.userId();
        System.out.println(userId);
        System.out.println("---------------------------------------");
        String profileId = request.profileId();
        return matchService.createNewMatch(userId, profileId);
    }

    public record CreateMatchRequest(
            String userId,
            String profileId
    ) {}


    @GetMapping("/{userId}/matches")
    public List<Match> getAllMatches(@PathVariable String userId){
        return matchService.getAllMatches(userId);
    }
}

package com.anuj.match_ai_backend.services;

import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.entities.matches.Match;
import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.entities.profiles.User;
import com.anuj.match_ai_backend.repositories.ConversationRepository;
import com.anuj.match_ai_backend.repositories.MatchRepository;
import com.anuj.match_ai_backend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.transform.sax.SAXResult;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MatchService {

    ProfileService profileService;
    UserService userService;
    ConversationRepository conversationRepository;
    MatchRepository matchRepository;
    UserRepository userRepository;

    public MatchService(ProfileService profileService, UserService userService, ConversationRepository conversationRepository, MatchRepository matchRepository, UserRepository userRepository) {
        this.profileService = profileService;
        this.userService = userService;
        this.conversationRepository = conversationRepository;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }



    public Match createNewMatch(String userId, String profileId) {

        User user = userService.getUserById(userId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find USer with the ID " + userId));

        Profile profile = profileService.getProfileById(profileId).
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
//        System.out.println(conversation.id());
        user.matches().add(match);
        user.conversations().put(conversation.id(), conversation);
        userRepository.save(user);
        System.out.println("matches: " + user.matches());

        conversationRepository.save(conversation);
        matchRepository.save(match);

        return match;
    }

    public List<Match> getAllMatches(String userId){
        User user = userService.getUserById(userId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find USer with the ID " + userId));

        return user.matches();
    }
}

package com.anuj.match_ai_backend.controllers;

import com.anuj.match_ai_backend.entities.conversations.ChatMessage;
import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.services.ConversationService;
import com.anuj.match_ai_backend.services.ProfileService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ConversationController {

    ConversationService conversationService;

    ProfileService profileService;


    @Autowired
    public ConversationController(ConversationService conversationService, ProfileService profileService) {
        this.conversationService = conversationService;
        this.profileService = profileService;

    }


    @PostMapping("/conversations")
    public Conversation createNewConversation(@RequestBody CreateConversationRequest request) {

        String profileId = request.profileId();

        return conversationService.createNewConversation(profileId);

    }

    @PostMapping("/conversations/{conversationId}")
    public Conversation addMessageToConversation(@PathVariable String conversationId, @RequestBody ChatMessage chatMessage) {

        return conversationService.addMessageToConversation(conversationId, chatMessage);


    }

    @GetMapping("/conversations")
    public List<Conversation> getAllConversations() {

        return conversationService.getAllConversations();

    }

    @GetMapping("/conversations/{conversationId}")
    public Conversation getConversationById(@PathVariable String conversationId) {

        return conversationService.getConversationById(conversationId);

    }

//    @GetMapping("/conversations/{conversationId}")
//    public Conversation getProfileResponse()


    //CreateConversationRequest contains entire postMapping data and out of which we are retrieving profile id
    public record CreateConversationRequest(
            String profileId
    ) {
    }


//    @GetMapping("/aiResponse")
//    public String aiResponse(){
//        return chatClient.prompt()
//                .user("Tell me a joke")
//                .call()
//                .content();
//    }


}

package com.anuj.match_ai_backend.services;


import com.anuj.match_ai_backend.controllers.ConversationController;
import com.anuj.match_ai_backend.entities.conversations.ChatMessage;
import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.repositories.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ConversationService {

    ConversationRepository conversationRepository;
    ProfileService profileService;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository, ProfileService profileService) {
        this.conversationRepository = conversationRepository;
        this.profileService = profileService;
    }

    public Conversation createNewConversation(String profileId) {

        profileService.findProfileById(profileId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Profile with the ID " + profileId));

        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                profileId,
                new ArrayList<>()
        );

        conversationRepository.save(conversation);
        return conversation;
    }

    public Conversation addMessageToConversation(String conversationId, ChatMessage chatMessage) {


        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Conversation with the ID " + conversationId));

        Profile profile = profileService.findProfileById(chatMessage.profileId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Profile with the ID " + chatMessage.profileId()));


        //we are doing this becausethe message might not contain date time
        ChatMessage thisChatMessage = new ChatMessage(
                chatMessage.messageText(),
                LocalDateTime.now(),
                profile.id()
        );
        conversation.messages().add(thisChatMessage);
        conversationRepository.save(conversation);
        return conversation;
    }


    public List<Conversation> getAllConversations(){
        return conversationRepository.findAll();
    }

    public Conversation getConversationById(String conversationId){
        return conversationRepository.findById(conversationId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Conversation with the ID " + conversationId));
    }


}

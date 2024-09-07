package com.anuj.match_ai_backend.services;


import com.anuj.match_ai_backend.controllers.ConversationController;
import com.anuj.match_ai_backend.entities.conversations.ChatMessage;
import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.repositories.ConversationRepository;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ProfileService profileService;
    private final OllamaChatModel chatClient;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository, ProfileService profileService, OllamaChatModel chatClient) {
        this.conversationRepository = conversationRepository;
        this.profileService = profileService;
        this.chatClient = chatClient;
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

        Profile user = profileService.findProfileById(chatMessage.authorId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Profile with the ID " + chatMessage.authorId()));

        Profile profile = profileService.findProfileById(conversation.profileId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Profile with the ID " + conversation.profileId()));


        //we are doing this becausethe message might not contain date time
        ChatMessage thisChatMessage = new ChatMessage(
                chatMessage.messageText(),
                LocalDateTime.now(),
                chatMessage.authorId()
        );
        conversation.messages().add(thisChatMessage);
        getProfileResponse(conversation, profile, user);
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

    public Conversation getProfileResponse(Conversation conversation, Profile profile, Profile user){
        SystemMessage systemMessage = new SystemMessage("Pretend to be a Tinder user");

        List<AbstractMessage> conversationMessages = conversation.messages().stream().map(message->{
            if(message.authorId().equals(profile.id())){
                return new AssistantMessage(message.messageText());
            }
            else {
                return new UserMessage(message.messageText());

            }
        }).toList();


        List<Message> allMessages = new ArrayList<>();
        allMessages.add(systemMessage);
        allMessages.addAll(conversationMessages);

        Prompt prompt = new Prompt(allMessages);
//        OllamaApi.ChatRequest request = new OllamaApi.ChatRequest(prompt);
//        ChatResponse response = ollamaClient.createCompletion(request);
        ChatResponse response = chatClient.call(prompt);

//        System.out.println("This is the response from from Ollama: ");
//        System.out.println(response.getResult().getOutput().getContent());

        conversation.messages().add(new ChatMessage(
                response.getResult().getOutput().getContent(),
                LocalDateTime.now(),
                profile.id()
        ));
        return conversation;
    }

}

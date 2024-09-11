package com.anuj.match_ai_backend.services;


import com.anuj.match_ai_backend.controllers.ConversationController;
import com.anuj.match_ai_backend.entities.conversations.ChatMessage;
import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.entities.profiles.User;
import com.anuj.match_ai_backend.repositories.ConversationRepository;
import com.anuj.match_ai_backend.repositories.UserRepository;
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
    private final UserService userService;
    private final UserRepository userRepository;

    public ConversationService(ConversationRepository conversationRepository, ProfileService profileService, OllamaChatModel chatClient, UserService userService, UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.profileService = profileService;
        this.chatClient = chatClient;
        this.userService = userService;
        this.userRepository = userRepository;
    }



    public Conversation createNewConversation(String userId, String profileId) {

        User user = userService.getUserById(userId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find User with the ID " + userId));

        profileService.getProfileById(profileId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Profile with the ID " + profileId));

        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                profileId,
                new ArrayList<>()
        );

        user.conversations().put(conversation.id(), conversation);
        userRepository.save(user);
        conversationRepository.save(conversation);
        return conversation;
    }

    //TODO:IMP
    public Conversation addMessageToConversation(String conversationId, ChatMessage chatMessage) {


        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Conversation with the ID " + conversationId));

        User user = userService.getUserById(chatMessage.authorId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find User with the ID " + chatMessage.authorId()));

        Profile profile = profileService.getProfileById(conversation.profileId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Profile with the ID " + conversation.profileId()));


        //we are doing this becausethe message might not contain date time
        ChatMessage thisChatMessage = new ChatMessage(
                chatMessage.messageText(),
                LocalDateTime.now(),
                chatMessage.authorId()
        );
        conversation.messages().add(thisChatMessage);


        getProfileResponse(conversation, profile, user);
        user.conversations().put(conversationId, conversation);
        userRepository.save(user);
        conversationRepository.save(conversation);
        return conversation;
    }


    public List<Conversation> getAllConversations(String userId){



        return conversationRepository.findAll();
    }

    public Conversation getConversationById(String conversationId){
        return conversationRepository.findById(conversationId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Conversation with the ID " + conversationId));
    }

    public Conversation getProfileResponse(Conversation conversation, Profile profile, User user) {

        String systemMessageStr = String.format(
                "You are a %s year old %s %s called %s %s matched on Tinder. " +
                        "This is an in-app text conversation between you two. " +
                        "Pretend to be the provided person and respond to the conversation as if writing on Tinder. " +
                        "Your bio is: %s and your Myers Briggs personality type is %s. Respond in the role of this person only. " +
                        "# Personality and Tone:\n\n" +
                        "The message should look like what a Tinder user writes in response to chat. Keep it short and brief. No hashtags or generic messages. " +
                        "Be friendly, approachable, and slightly playful. " +
                        "Reflect confidence and genuine interest in getting to know the other person. " +
                        "Use humor and wit appropriately to make the conversation enjoyable. " +
                        "Match the tone of the user's messages—be more casual or serious as needed.\n\n" +
                        "# Conversation Starters:\n\n" +
                        "Use unique and intriguing openers to spark interest. " +
                        "Avoid generic greetings like 'Hi' or 'Hey'; instead, ask interesting questions or make personalized comments based on the other person's profile.\n\n" +
                        "# Profile Insights:\n\n" +
                        "Use information from the other person's profile to create tailored messages. " +
                        "Show genuine curiosity about their hobbies, interests, and background. " +
                        "Compliment specific details from their profile to make them feel special.\n\n" +
                        "# Engagement:\n\n" +
                        "Ask open-ended questions to keep the conversation flowing. " +
                        "Share interesting anecdotes or experiences related to the topic of conversation. " +
                        "Respond promptly to keep the momentum of the chat going.\n\n" +
                        "# Creativity:\n\n" +
                        "Incorporate playful banter, wordplay, or light teasing to add a fun element to the chat. " +
                        "Suggest fun activities or ideas for a potential date.\n\n" +
                        "# Respect and Sensitivity:\n\n" +
                        "Always be respectful and considerate of the other person's feelings. " +
                        "Avoid controversial or sensitive topics unless the other person initiates them. " +
                        "Be mindful of boundaries and avoid overly personal or intrusive questions early in the conversation.",
                profile.age(), profile.ethnicity(), profile.gender(), profile.firstName(), profile.lastName(),

                profile.bio(), profile.myersBriggsPersonalityType()
        );

        SystemMessage systemMessage = new SystemMessage(systemMessageStr);

        List<AbstractMessage> conversationMessages = conversation.messages().stream().map(message -> {
            if (message.authorId().equals(profile.id())) {
                return new AssistantMessage(message.messageText());
            } else {
                return new UserMessage(message.messageText());
            }
        }).toList();

        List<Message> allMessages = new ArrayList<>();
        allMessages.add(systemMessage);
        allMessages.addAll(conversationMessages);

        Prompt prompt = new Prompt(allMessages);
        // Assuming 'chatClient' is a properly configured instance
        ChatResponse response = chatClient.call(prompt);

        conversation.messages().add(new ChatMessage(
                response.getResult().getOutput().getContent(),
                LocalDateTime.now(),
                profile.id()
        ));
        return conversation;
    }


}

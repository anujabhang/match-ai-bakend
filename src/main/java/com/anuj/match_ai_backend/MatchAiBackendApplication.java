package com.anuj.match_ai_backend;

import com.anuj.match_ai_backend.entities.conversations.ChatMessage;
import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.entities.profiles.Gender;
import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.repositories.ConversationRepository;
import com.anuj.match_ai_backend.repositories.MatchRepository;
import com.anuj.match_ai_backend.repositories.ProfileRepository;
import com.anuj.match_ai_backend.services.ProfileCreationService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MatchAiBackendApplication implements CommandLineRunner{

	ProfileCreationService profileCreationService;
	ProfileRepository profileRepository;
	ConversationRepository conversationRepository;
	MatchRepository matchRepository;


	public MatchAiBackendApplication(ProfileCreationService profileCreationService, ProfileRepository profileRepository, ConversationRepository conversationRepository, MatchRepository matchRepository) {
		this.profileCreationService = profileCreationService;
		this.profileRepository = profileRepository;
		this.conversationRepository = conversationRepository;
		this.matchRepository = matchRepository;
	}


	public static void main(String[] args) {

		SpringApplication.run(MatchAiBackendApplication.class, args);





	}


	@Override
	public void run(String... args) {
//		clearAllData();
//		profileCreationService.saveProfilesToDB();

	}

	private void clearAllData() {
		conversationRepository.deleteAll();
			matchRepository.deleteAll();
		profileRepository.deleteAll();
	}
}

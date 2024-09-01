package com.anuj.match_ai_backend;

import com.anuj.match_ai_backend.entities.conversations.ChatMessage;
import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.entities.profiles.Gender;
import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.repositories.ConversationRepository;
import com.anuj.match_ai_backend.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MatchAiBackendApplication implements CommandLineRunner {

	@Autowired
	ProfileRepository profileRepository;

	@Autowired
	ConversationRepository conversationRepository;

	public static void main(String[] args) {
		SpringApplication.run(MatchAiBackendApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Profile profile = new Profile(
			"1",
				"Anuj",
				"Abhang",
				21,
				"Mongolian",
				Gender.Male,
				"Love to Travel",
				"hi.jpg",
				"INTP"

		);


		Conversation conversation = new Conversation(
				"1",
				profile.id(),

				List.of(
						new ChatMessage(
								"Hello MatchAI",
								LocalDateTime.now(),
								profile.id()
						)
				)
		);

		profileRepository.save(profile);
		profileRepository.findAll().forEach(System.out::println);

		conversationRepository.save(conversation);
		conversationRepository.findAll().forEach(System.out::println);
	}
}

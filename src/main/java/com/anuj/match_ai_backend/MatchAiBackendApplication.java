package com.anuj.match_ai_backend;

import com.anuj.match_ai_backend.entities.conversations.ChatMessage;
import com.anuj.match_ai_backend.entities.conversations.Conversation;
import com.anuj.match_ai_backend.entities.profiles.Gender;
import com.anuj.match_ai_backend.entities.profiles.Profile;
import com.anuj.match_ai_backend.repositories.ConversationRepository;
import com.anuj.match_ai_backend.repositories.ProfileRepository;
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
public class MatchAiBackendApplication {

//	private static OllamaChatModel chatModel;
//
//
//	@Autowired
//	public MatchAiBackendApplication(OllamaChatModel chatModel) {
//		this.chatModel = chatModel;
//	}

	public static void main(String[] args) {

		SpringApplication.run(MatchAiBackendApplication.class, args);


//		Prompt prompt = new Prompt("Who is Anuj Abhang");
//		ChatResponse response = chatModel.call(prompt);
//		System.out.println(response.getResult().getOutput());




	}
}

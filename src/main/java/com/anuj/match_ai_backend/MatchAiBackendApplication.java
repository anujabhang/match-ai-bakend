package com.anuj.match_ai_backend;

import com.anuj.match_ai_backend.entities.Profiles.Gender;
import com.anuj.match_ai_backend.entities.Profiles.Profile;
import com.anuj.match_ai_backend.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchAiBackendApplication implements CommandLineRunner {

	@Autowired
	ProfileRepository profileRepository;

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

		profileRepository.save(profile);
		profileRepository.findAll().forEach(System.out::println);
	}
}

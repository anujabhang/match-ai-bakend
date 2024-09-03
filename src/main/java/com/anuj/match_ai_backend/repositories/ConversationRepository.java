package com.anuj.match_ai_backend.repositories;

import com.anuj.match_ai_backend.entities.conversations.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
}

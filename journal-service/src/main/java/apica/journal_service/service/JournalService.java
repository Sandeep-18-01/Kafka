package apica.journal_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import apica.journal_service.dto.UserEvent;
import apica.journal_service.entity.JournalEntity;
import apica.journal_service.repository.JournalRepository;

import java.time.LocalDateTime;

@Service
public class JournalService {

    private final JournalRepository repository;

    public JournalService(JournalRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "user-events", groupId = "journal-group")
    public void listen(UserEvent event) {
        JournalEntity entry = new JournalEntity();
        entry.setUserId(event.getId());
        entry.setUsername(event.getUsername());
        entry.setEmail(event.getEmail());
        entry.setRole(event.getRole());
        entry.setEventType(event.getEventType());
        entry.setTimestamp(LocalDateTime.now());

        repository.save(entry);
        System.out.println("📥 Saved journal entry: " + entry.getEventType() + " for user " + entry.getUsername());
    }
}

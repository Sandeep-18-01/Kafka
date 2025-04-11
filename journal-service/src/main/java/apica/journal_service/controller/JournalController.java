package apica.journal_service.controller;

import org.springframework.web.bind.annotation.*;

import apica.journal_service.entity.JournalEntity;
import apica.journal_service.repository.JournalRepository;

import java.util.List;

@RestController
@RequestMapping("/journals")
public class JournalController {

    private final JournalRepository repository;

    public JournalController(JournalRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<JournalEntity> getAll() {
        return repository.findAll();
    }
}

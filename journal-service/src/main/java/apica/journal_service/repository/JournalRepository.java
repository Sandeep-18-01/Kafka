package apica.journal_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import apica.journal_service.entity.JournalEntity;

public interface JournalRepository extends JpaRepository<JournalEntity, Long> {
}

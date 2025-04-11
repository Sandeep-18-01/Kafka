package apica.user_service.service;

import apica.user_service.kafka.UserEvent;
import apica.user_service.kafka.UserKafkaProducer;
import apica.user_service.model.User;
import apica.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserKafkaProducer kafkaProducer;

    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        UserEvent event = buildUserEvent(savedUser, "CREATE");
        kafkaProducer.sendUserEvent(event);
        log.info("User created with ID: {}", savedUser.getId());
        return savedUser;
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users...");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setRole(updatedUser.getRole());

            User savedUser = userRepository.save(existingUser);
            kafkaProducer.sendUserEvent(buildUserEvent(savedUser, "UPDATE"));
            log.info("User updated with ID: {}", id);
            return savedUser;
        });
    }

    public boolean deleteUser(Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            kafkaProducer.sendUserEvent(buildUserEvent(user, "DELETE"));
            log.info("User deleted with ID: {}", id);
            return true;
        }).orElse(false);
    }

    private UserEvent buildUserEvent(User user, String eventType) {
        return UserEvent.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .eventType(eventType)
                .build();
    }
}

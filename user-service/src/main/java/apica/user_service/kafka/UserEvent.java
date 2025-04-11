package apica.user_service.kafka;

import apica.user_service.model.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEvent {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private String eventType; // CREATE, UPDATE, DELETE
}

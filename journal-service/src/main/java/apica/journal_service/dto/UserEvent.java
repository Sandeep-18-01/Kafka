package apica.journal_service.dto;

import lombok.Data;

@Data
public class UserEvent {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String eventType;

}

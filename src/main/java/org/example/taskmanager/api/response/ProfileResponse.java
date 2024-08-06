package org.example.taskmanager.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private UUID id;
    private String name;
    private String email;
    private Boolean isActive;
}

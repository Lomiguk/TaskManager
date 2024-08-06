package org.example.taskmanager.api.request;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class PutProfileRequest {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private Boolean isActive;
}

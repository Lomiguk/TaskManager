package org.example.taskmanager.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PutProfileRequest {
    private String name;
    private String email;
    private String password;
    private Boolean isActive;
}

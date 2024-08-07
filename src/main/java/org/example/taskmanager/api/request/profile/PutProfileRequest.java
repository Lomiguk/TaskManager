package org.example.taskmanager.api.request.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PutProfileRequest {

    @NotNull
    @Size(min = 1, max = 100, message = "Wrong profile name size")
    private String name;
    @Email
    @NotNull
    private String email;
    @Email
    @NotNull
    private String password;
    @Size(min = 5)
    private Boolean isActive;
}

package org.example.taskmanager.api.request.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.taskmanager.api.enums.TaskPriority;
import org.example.taskmanager.api.enums.TaskStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PutTaskRequest {

    @NotNull
    @Size(min = 1)
    private String label;
    private String description;
    @NotNull
    private TaskStatus status;
    @NotNull
    private TaskPriority priority;
    @NotNull
    private UUID authorId;
    private UUID executorId;
}

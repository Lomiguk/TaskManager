package org.example.taskmanager.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.taskmanager.api.enums.TaskPriority;
import org.example.taskmanager.api.enums.TaskStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TaskResponse {

    private UUID id;
    private String label;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UUID authorId;
    private UUID executorId;
}

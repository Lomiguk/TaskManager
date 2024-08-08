package org.example.taskmanager.api.request.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.taskmanager.api.enums.TaskPriority;
import org.example.taskmanager.api.enums.TaskStatus;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PutTaskRequest {

    @NotNull
    @Size(min = 1, message = "Min of label size is 1")
    private String label;
    private String description;
    @NotNull (message = "Status can not be null (SELECT: WAITING, PROCESSING, COMPLETE)")
    private TaskStatus status;
    @NotNull (message = "Priority can not be null (SELECT: HIGH, MEDIUM, LOW)")
    private TaskPriority priority;
    @NotNull (message = "Author can not be null (SEND author's UUID)")
    private UUID authorId;
    private UUID executorId;
}

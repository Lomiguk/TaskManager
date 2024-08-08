package org.example.taskmanager.api.request.taskComment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AddTaskCommentRequest {

    @NotNull
    private UUID authorId;
    @NotNull
    private UUID taskId;
    @NotNull
    @Size(min = 1, max = 200)
    private String content;
}

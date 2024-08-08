package org.example.taskmanager.api.request.taskComment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class PutTaskCommentRequest {

    @NotNull
    private final UUID authorId;
    @NotNull
    private final UUID taskId;
    @NotNull
    @Size(min = 1, max = 200)
    private final String content;
}
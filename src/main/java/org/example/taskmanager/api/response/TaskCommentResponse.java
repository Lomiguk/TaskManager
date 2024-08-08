package org.example.taskmanager.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCommentResponse {

    private UUID id;
    private UUID authorId;
    private UUID taskId;
    private String content;
}

package org.example.taskmanager.service.interfaces;

import org.example.taskmanager.api.request.taskComment.AddTaskCommentRequest;
import org.example.taskmanager.api.request.taskComment.PutTaskCommentRequest;
import org.example.taskmanager.api.response.TaskCommentResponse;

import java.util.Collection;
import java.util.UUID;

public interface TaskCommentService {

    TaskCommentResponse create(AddTaskCommentRequest addTaskCommentRequest);

    TaskCommentResponse getById(UUID id);

    Collection<TaskCommentResponse> getAllByTaskWithPagination(
            UUID taskId,
            Integer pageNumber,
            Integer pageSize
    );

    TaskCommentResponse update(
            UUID id,
            PutTaskCommentRequest request);

    Boolean delete(UUID id);
}

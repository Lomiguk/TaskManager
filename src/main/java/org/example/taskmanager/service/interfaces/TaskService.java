package org.example.taskmanager.service.interfaces;

import org.example.taskmanager.api.request.task.AddTaskRequest;
import org.example.taskmanager.api.request.task.PutTaskRequest;
import org.example.taskmanager.api.response.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public interface TaskService {

    TaskResponse create(AddTaskRequest request);

    TaskResponse getById(UUID id);

    Collection<TaskResponse> getAllWithPagination(Integer pageSize, Integer pageNumber);

    TaskResponse putUpdate(UUID id, PutTaskRequest request);

    Boolean delete(UUID id);

    Collection<TaskResponse> getByProfile(UUID id, String status);

    TaskResponse patchStatus(UUID id, String status);
}

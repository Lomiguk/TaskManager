package org.example.taskmanager.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.task.AddTaskRequest;
import org.example.taskmanager.api.request.task.PutTaskRequest;
import org.example.taskmanager.api.response.TaskResponse;
import org.example.taskmanager.entity.Task;
import org.example.taskmanager.exception.ExpectedEntityNotFoundException;
import org.example.taskmanager.repository.TaskDAO;
import org.example.taskmanager.service.interfaces.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskDAO taskDAO;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TaskResponse create(AddTaskRequest request) {
        var id = UUID.randomUUID();
        var taskEntity = convertToEntity(id, request);
        taskDAO.save(taskEntity);

        return getById(id);
    }

    @Override
    @Transactional
    public TaskResponse getById(UUID id) {
        return convertToResponse(getEntity(id));
    }

    @Override
    public Collection<TaskResponse> getAllWithPagination(Integer pageSize, Integer pageNumber) {
        return taskDAO.findAll(PageRequest.of(pageNumber, pageSize)).stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public Collection<TaskResponse> getByAuthorProfile(UUID profileId) {
        var tasks = taskDAO.findTaskByAuthorId(profileId);
        return tasks.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public Collection<TaskResponse> getByExecutorProfile(UUID profileId) {
        var tasks = taskDAO.findTaskByExecutorId(profileId);
        return tasks.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional
    public TaskResponse putUpdate(UUID id, PutTaskRequest request) {
        taskDAO.save(convertToEntity(id, request));
        return getById(id);
    }

    @Override
    @Transactional
    public Boolean delete(UUID id) {
        taskDAO.deleteById(id);
        return !taskDAO.existsById(id);
    }

    private Task getEntity(UUID id) {
        return taskDAO.findById(id).orElseThrow(
                () -> new ExpectedEntityNotFoundException(String.format("Profile (%s) not found", id))
        );
    }

    private <T> Task convertToEntity(UUID id, T dto) {
        var task = modelMapper.map(dto, Task.class);
        if (task.getId() == null) task.setId(id);
        return task;
    }

    private TaskResponse convertToResponse(Task entity) {
        return modelMapper.map(entity, TaskResponse.class);
    }
}

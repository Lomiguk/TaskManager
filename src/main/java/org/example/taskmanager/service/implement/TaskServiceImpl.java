package org.example.taskmanager.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.enums.ProfileStatusForTask;
import org.example.taskmanager.api.enums.TaskStatus;
import org.example.taskmanager.api.request.task.AddTaskRequest;
import org.example.taskmanager.api.request.task.PutTaskRequest;
import org.example.taskmanager.api.response.TaskResponse;
import org.example.taskmanager.entity.Task;
import org.example.taskmanager.exception.ExpectedEntityNotFoundException;
import org.example.taskmanager.exception.UnexpectedRequestParameterException;
import org.example.taskmanager.repository.ProfileDAO;
import org.example.taskmanager.repository.TaskCommentDAO;
import org.example.taskmanager.repository.TaskDAO;
import org.example.taskmanager.service.interfaces.TaskService;
import org.example.taskmanager.util.ProfileAccessUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = Logger.getLogger(TaskServiceImpl.class.getName());

    private final TaskDAO taskDAO;
    private final ProfileDAO profileDAO;
    private final ModelMapper modelMapper;
    private final TaskCommentDAO taskCommentDAO;
    private final ProfileAccessUtil profileAccessUtil;

    @Override
    @Transactional
    public TaskResponse create(AddTaskRequest request) {
        // check
        //TODO add ADMIN status to Profile
        //TODO allow to admin profile creating tasks with all profile on author position
        profileAccessUtil.checkAuthorAuthorization(Set.of(request.getAuthorId()));
        checkAuthorExecutorFKConstraint(request.getAuthorId(), request.getExecutorId());
        // logic
        var id = UUID.randomUUID();
        var taskEntity = convertToEntity(id, request);

        return convertToResponse(taskDAO.save(taskEntity));
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
    @Transactional
    public TaskResponse putUpdate(UUID id, PutTaskRequest request) {
        // check
        profileAccessUtil.checkAuthorAuthorization(Set.of(request.getAuthorId()));
        checkAuthorExecutorFKConstraint(request.getAuthorId(), request.getExecutorId());
        // logic
        return convertToResponse(taskDAO.save(convertToEntity(id, request)));
    }

    @Override
    @Transactional
    public TaskResponse patchStatus(UUID id, String status) {
        // prepare
        var task = getEntity(id);
        //check
        profileAccessUtil.checkAuthorAuthorization(Set.of(task.getAuthorId(), task.getExecutorId()));
        //logic
        try {
            task.setStatus(TaskStatus.valueOf(status));
            return convertToResponse(taskDAO.save(task));
        } catch (IllegalArgumentException ex) {
            throw new UnexpectedRequestParameterException(
                    String.format(
                            "%s - unknown task status. Existed values: %s",
                            status,
                            Arrays.stream(TaskStatus.values())
                                    .map(Enum::name)
                                    .collect(Collectors.joining(", "))
                    )
            );
        }
    }

    @Override
    @Transactional
    public Boolean delete(UUID id) {
        // check
        profileAccessUtil.checkAuthorAuthorization(Set.of(getById(id).getAuthorId()));
        // logic
        taskCommentDAO.deleteAllByTaskId(id);
        taskDAO.deleteById(id);
        return !taskDAO.existsById(id);
    }

    @Override
    public Collection<TaskResponse> getByProfile(UUID id, String status) {
        Collection<TaskResponse> result;
        try {
            result = switch (ProfileStatusForTask.valueOf(status)) {
                case AUTHOR -> getByAuthorProfile(id);
                case EXECUTOR -> getByExecutorProfile(id);
            };
        } catch (IllegalArgumentException ex) {
            throw new UnexpectedRequestParameterException(
                    String.format(
                            "%s - unknown profile status. Existed values: %s",
                            status,
                            Arrays.stream(ProfileStatusForTask.values())
                                    .map(Enum::name)
                                    .collect(Collectors.joining(", "))
                    )
            );
        }

        return result;
    }

    private Collection<TaskResponse> getByAuthorProfile(UUID profileId) {
        var tasks = taskDAO.findTaskByAuthorId(profileId);
        return tasks.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private Collection<TaskResponse> getByExecutorProfile(UUID profileId) {
        var tasks = taskDAO.findTaskByExecutorId(profileId);
        return tasks.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private void checkAuthorExecutorFKConstraint(UUID authorId, UUID executorId) {
        if (!profileDAO.existsById(authorId)) {
            throw new ExpectedEntityNotFoundException(String.format("Author's profile with ID (%s) not found", authorId));
        }
        if (executorId != null && !profileDAO.existsById(executorId)) {
            throw new ExpectedEntityNotFoundException(String.format("Executor's profile with ID (%s) not found", executorId));
        }
    }

    private Task getEntity(UUID id) {
        return taskDAO.findById(id).orElseThrow(
                () -> new ExpectedEntityNotFoundException(String.format("Task with ID (%s) not found", id))
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

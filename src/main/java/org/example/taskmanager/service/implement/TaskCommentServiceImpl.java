package org.example.taskmanager.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.taskComment.AddTaskCommentRequest;
import org.example.taskmanager.api.request.taskComment.PutTaskCommentRequest;
import org.example.taskmanager.api.response.TaskCommentResponse;
import org.example.taskmanager.entity.TaskComment;
import org.example.taskmanager.exception.ExpectedEntityNotFoundException;
import org.example.taskmanager.repository.ProfileDAO;
import org.example.taskmanager.repository.TaskCommentDAO;
import org.example.taskmanager.repository.TaskDAO;
import org.example.taskmanager.service.interfaces.TaskCommentService;
import org.example.taskmanager.util.ProfileAccessUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskCommentServiceImpl implements TaskCommentService {

    private final TaskCommentDAO taskCommentDAO;
    private final ProfileDAO profileDAO;
    private final TaskDAO taskDAO;
    private final ModelMapper modelMapper;
    private final ProfileAccessUtil profileAccessUtil;

    @Override
    @Transactional
    public TaskCommentResponse create(
            AddTaskCommentRequest request
    ) {
        // check
        checkAuthorFKConstraint(request.getAuthorId());
        checkTaskFKConstraint(request.getTaskId());
        profileAccessUtil.checkAuthorAuthorization(Set.of(request.getAuthorId()));
        // logic
        var id = UUID.randomUUID();
        var taskComment = convertToEntity(id, request);
        taskCommentDAO.save(taskComment);

        return getById(id);
    }

    @Override
    public TaskCommentResponse getById(UUID id) {
        return convertToResponse(getEntity(id));
    }

    @Override
    @Transactional
    public Collection<TaskCommentResponse> getAllByTaskWithPagination(
            UUID taskId,
            Integer pageNumber,
            Integer pageSize
    ) {
        // check
        checkTaskFKConstraint(taskId);
        // logic
        return taskCommentDAO.findAllByTaskId(taskId, PageRequest.of(pageNumber, pageSize)).stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional
    public TaskCommentResponse update(UUID id, PutTaskCommentRequest request) {
        // check
        checkAuthorFKConstraint(request.getAuthorId());
        checkTaskFKConstraint(request.getTaskId());
        profileAccessUtil.checkAuthorAuthorization(Set.of(request.getAuthorId()));
        // logic
        taskCommentDAO.save(convertToEntity(id, request));
        return getById(id);
    }

    @Override
    public Boolean delete(UUID id) {
        // check
        profileAccessUtil.checkAuthorAuthorization(Set.of(getEntity(id).getAuthorId()));
        // logic
        taskCommentDAO.deleteById(id);
        return !taskCommentDAO.existsById(id);
    }

    private TaskComment getEntity(UUID id) {
        return taskCommentDAO.findById(id).orElseThrow(
                () -> new ExpectedEntityNotFoundException(String.format("Task comment with ID (%s) not found", id))
        );
    }

    private <T> TaskComment convertToEntity(UUID id, T dto) {
        var entity = modelMapper.map(dto, TaskComment.class);
        if (entity.getId() == null) entity.setId(id);
        return entity;
    }

    private TaskCommentResponse convertToResponse(TaskComment entity) {
        return modelMapper.map(entity, TaskCommentResponse.class);
    }

    private void checkAuthorFKConstraint(UUID authorId) {
        if (!profileDAO.existsById(authorId)) {
            throw new ExpectedEntityNotFoundException(String.format("Author's profile with ID (%s) not found", authorId));
        }
    }

    private void checkTaskFKConstraint(UUID taskId) {
        if (!taskDAO.existsById(taskId)) {
            throw new ExpectedEntityNotFoundException(String.format("Task with ID (%s) not found", taskId));
        }
    }
}

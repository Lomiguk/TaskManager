package org.example.taskmanager.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.profile.AddProfileRequest;
import org.example.taskmanager.api.request.profile.PutProfileRequest;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.entity.Profile;
import org.example.taskmanager.entity.Task;
import org.example.taskmanager.exception.ExpectedEntityNotFoundException;
import org.example.taskmanager.repository.ProfileDAO;
import org.example.taskmanager.repository.TaskCommentDAO;
import org.example.taskmanager.repository.TaskDAO;
import org.example.taskmanager.service.interfaces.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileDAO profileDAO;
    private final ModelMapper modelMapper;
    private final TaskDAO taskDAO;
    private final TaskCommentDAO taskCommentDAO;

    @Override
    @Transactional
    public ProfileResponse create(AddProfileRequest profile) {
        var id = UUID.randomUUID();
        profileDAO.save(convertToEntity(id, profile));
        return getById(id);
    }

    @Override
    public ProfileResponse getById(UUID id) {
        return convertToResponse(getEntity(id));
    }

    @Override
    @Transactional
    public ProfileResponse update(UUID id, PutProfileRequest request) {
        profileDAO.save(convertToEntity(id, request));
        return getById(id);
    }

    @Override
    @Transactional
    public Boolean delete(UUID id) {
        if (thereAreNoTasksWithProfileAsAuthor(id) && thereAreNoTaskCommentsFromProfile(id)) {
            if (!thereAreNoTasksWithProfileAsExecutor(id)) {
                removeProfileFromTaskAsExecutor(id);
            }
            profileDAO.deleteById(id);
        } else {
            var deactivatedProfile = getEntity(id);
            deactivatedProfile.setIsActive(false);
            profileDAO.save(deactivatedProfile);
        }

        var deletableProfile = profileDAO.findById(id);
        return deletableProfile.isEmpty() || !deletableProfile.get().getIsActive();
    }

    @Override
    public Collection<ProfileResponse> getAllWithPagination(Integer pageSize, Integer pageNumber) {
        return profileDAO.findAll(PageRequest.of(pageNumber, pageSize)).stream()
                .map(this::convertToResponse)
                .toList();

    }

    private void removeProfileFromTaskAsExecutor(UUID profileId) {
        Collection<Task> tasks = taskDAO.findTaskByExecutorId(profileId);
        tasks.stream().map(it -> {
                    var newTaskVer = it.clone();
                    newTaskVer.setExecutorId(null);
                    return newTaskVer;
                })
                .toList().forEach(taskDAO::save);
    }

    private Boolean thereAreNoTasksWithProfileAsAuthor(UUID profileId) {
        return !taskDAO.existsByAuthorId(profileId);
    }

    private Boolean thereAreNoTasksWithProfileAsExecutor(UUID profileId) {
        return !taskDAO.existsByExecutorId(profileId);
    }

    private Boolean thereAreNoTaskCommentsFromProfile(UUID profileID) {
        return !taskCommentDAO.existsByAuthorId(profileID);
    }

    private Profile getEntity(UUID id) {
        return profileDAO.findById(id).orElseThrow(
                () -> new ExpectedEntityNotFoundException(String.format("Profile (%s) not found", id))
        );
    }

    private <T> Profile convertToEntity(UUID id, T dto) {
        var profile = modelMapper.map(dto, Profile.class);
        if (profile.getId() == null) profile.setId(id);
        if (profile.getIsActive() == null) profile.setIsActive(true);
        return profile;
    }

    private ProfileResponse convertToResponse(Profile entity) {
        return modelMapper.map(entity, ProfileResponse.class);
    }
}

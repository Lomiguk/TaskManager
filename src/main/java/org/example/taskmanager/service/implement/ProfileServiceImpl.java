package org.example.taskmanager.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.profile.AddProfileRequest;
import org.example.taskmanager.api.request.profile.PutProfileRequest;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.entity.Profile;
import org.example.taskmanager.entity.Task;
import org.example.taskmanager.repository.ProfileDAO;
import org.example.taskmanager.repository.TaskCommentDAO;
import org.example.taskmanager.repository.TaskDAO;
import org.example.taskmanager.service.interfaces.ProfileService;
import org.example.taskmanager.util.PasswordUtil;
import org.example.taskmanager.util.ProfileAccessUtil;
import org.example.taskmanager.util.ProfileUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private static final Logger LOGGER = Logger.getLogger(ProfileServiceImpl.class.getName());

    private final ProfileDAO profileDAO;
    private final ProfileUtil profileUtil;
    private final TaskDAO taskDAO;
    private final TaskCommentDAO taskCommentDAO;
    private final ProfileAccessUtil profileAccessUtil;
    private final PasswordUtil passwordUtil;

    @Override
    @Transactional
    public ProfileResponse create(AddProfileRequest request) {
        //TODO add permission status to Profile
        //TODO checkAdminPermission on FilterChain level

        var id = UUID.randomUUID();
        profileDAO.save(new Profile(
                id,
                request.getName(),
                request.getEmail(),
                passwordUtil.getEncodedPassword(request.getPassword()),
                true
        ));
        return getById(id);
    }

    @Override
    public ProfileResponse getById(UUID id) {
        return profileUtil.tryToGetProfile(id);
    }

    @Override
    @Transactional
    public ProfileResponse update(UUID id, PutProfileRequest request) {
        // check
        profileAccessUtil.checkAuthorAuthorization(id);
        // logic
        profileDAO.save(profileUtil.convertToEntity(id, request));
        return getById(id);
    }

    @Override
    @Transactional
    public Boolean delete(UUID id) {
        // check
        profileAccessUtil.checkAuthorAuthorization(id);
        // logic
        if (thereAreNoTasksWithProfileAsAuthor(id) && thereAreNoTaskCommentsFromProfile(id)) {
            if (!thereAreNoTasksWithProfileAsExecutor(id)) {
                removeProfileFromTaskAsExecutor(id);
            }
            profileDAO.deleteById(id);
        } else {
            var deactivatedProfile = profileUtil.getEntity(id);
            deactivatedProfile.setIsActive(false);
            profileDAO.save(deactivatedProfile);
        }

        var deletableProfile = profileDAO.findById(id);
        return deletableProfile.isEmpty() || !deletableProfile.get().getIsActive();
    }

    @Override
    public Collection<ProfileResponse> getAllWithPagination(Integer pageSize, Integer pageNumber) {
        return profileDAO.findAll(PageRequest.of(pageNumber, pageSize)).stream()
                .map(profileUtil::convertToResponse)
                .toList();

    }

    private void removeProfileFromTaskAsExecutor(UUID profileId) {
        Collection<Task> tasks = taskDAO.findTaskByExecutorId(profileId);
        tasks.stream()
                .map(it -> {
                    var newTaskVer = it.clone();
                    newTaskVer.setExecutorId(null);
                    return newTaskVer;
                })
                .forEach(taskDAO::save);
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
}

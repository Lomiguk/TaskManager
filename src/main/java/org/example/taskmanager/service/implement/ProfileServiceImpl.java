package org.example.taskmanager.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.profile.AddProfileRequest;
import org.example.taskmanager.api.request.profile.PutProfileRequest;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.entity.Profile;
import org.example.taskmanager.exception.ExpectedEntityNotFoundException;
import org.example.taskmanager.exception.UniqueConstrainViolationException;
import org.example.taskmanager.repository.ProfileDAO;
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

    @Override
    @Transactional
    public ProfileResponse create(AddProfileRequest request) {
        checkForEmailUniqueConstraint(request.getEmail());

        var id = UUID.randomUUID();

        profileDAO.save(convertToEntity(id, request));
        return get(id);
    }

    @Override
    public ProfileResponse get(UUID id) {
        return convertToResponse(getEntity(id));
    }

    @Override
    @Transactional
    public ProfileResponse update(UUID id, PutProfileRequest request) {
        checkForEmailUniqueConstraint(request.getEmail());

        profileDAO.save(convertToEntity(id, request));
        return get(id);
    }

    @Override
    public Boolean delete(UUID id) {
        profileDAO.deleteById(id);
        return !profileDAO.existsById(id);
    }

    @Override
    public Collection<ProfileResponse> getAllWithPagination(Integer pageSize, Integer pageNumber) {
        return profileDAO.findAll(PageRequest.of(pageNumber, pageSize)).stream()
                .map(this::convertToResponse)
                .toList();

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

    private void checkForEmailUniqueConstraint(String email) {
        var isExisted = profileDAO.existsProfileByEmail(email);

        if (isExisted) {
            throw new UniqueConstrainViolationException(String.format("Email - %s already exists", email));
        }
    }
}

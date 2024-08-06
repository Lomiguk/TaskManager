package org.example.taskmanager.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.ProfileRequest;
import org.example.taskmanager.api.request.PutProfileRequest;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.entity.Profile;
import org.example.taskmanager.exception.ExpectedEntityNotFoundException;
import org.example.taskmanager.repository.ProfileDAO;
import org.example.taskmanager.service.interfaces.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileDAO profileDAO;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ProfileResponse create(ProfileRequest profile) {
        var id = UUID.randomUUID();
        profileDAO.save(convertToEntity(id, profile));
        return get(id);
    }

    @Override
    public ProfileResponse get(UUID id) {
        return convertToResponse(getEntity(id));
    }

    @Override
    public Collection<ProfileResponse> get() {
        return List.of();
    }

    @Override
    @Transactional
    public ProfileResponse update(UUID id, PutProfileRequest request) {
        profileDAO.save(convertToEntity(id, request));
        return get(id);
    }

    @Override
    public Boolean delete(UUID id) {
        profileDAO.deleteById(id);
        return !profileDAO.existsById(id);
    }

    @Override
    public Collection<ProfileResponse> getAllWithPagination(Integer limit, Integer offset) {
        return profileDAO.findAllWithPagination(limit, offset).stream()
                .map(this::convertToResponse)
                .toList();
    }

    private Profile getEntity(UUID id) {
        return profileDAO.findById(id).orElseThrow(
                () -> new ExpectedEntityNotFoundException(String.format("Profile (%s) not found", id))
        );
    }

    private <T> Profile convertToEntity(UUID id, T dto) {
        Profile profile = modelMapper.map(dto, Profile.class);
        if (profile.getId() == null) profile.setId(id);
        if (profile.getIsActive() == null) profile.setIsActive(true);
        return profile;
    }

    private ProfileResponse convertToResponse(Profile entity) {
        return modelMapper.map(entity, ProfileResponse.class);
    }
}

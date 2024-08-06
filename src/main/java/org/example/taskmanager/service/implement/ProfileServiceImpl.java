package org.example.taskmanager.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.AddProfileRequest;
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
    public ProfileResponse create(AddProfileRequest profile) {
        var id = UUID.randomUUID();
        profileDAO.save(convertToEntity(profile, id));
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
    public ProfileResponse update(UUID id, PutProfileRequest request) {
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        return null;
    }

    private Profile getEntity(UUID id) {
        return profileDAO.findById(id).orElseThrow(
                () -> new ExpectedEntityNotFoundException(String.format("Profile (%s) not found", id))
        );
    }

    private Profile convertToEntity(AddProfileRequest dto, UUID id) {
        Profile profile = modelMapper.map(dto, Profile.class);
        if (profile.getId() == null) profile.setId(id);
        if (profile.getIsActive() == null) profile.setIsActive(true);
        return profile;
    }

    private ProfileResponse convertToResponse(Profile entity) {
        return modelMapper.map(entity, ProfileResponse.class);
    }
}

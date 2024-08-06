package org.example.taskmanager.service.interfaces;

import org.example.taskmanager.api.request.profile.AddProfileRequest;
import org.example.taskmanager.api.request.profile.PutProfileRequest;
import org.example.taskmanager.api.response.ProfileResponse;

import java.util.Collection;
import java.util.UUID;

public interface ProfileService {

    ProfileResponse create(
            AddProfileRequest request
    );

    ProfileResponse get(UUID id);

    ProfileResponse update(
            UUID id,
            PutProfileRequest request
    );

    Boolean delete(UUID id);

    Collection<ProfileResponse> getAllWithPagination(Integer pageSize, Integer pageNumber);
}

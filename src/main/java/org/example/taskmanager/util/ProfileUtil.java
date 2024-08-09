package org.example.taskmanager.util;

import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.entity.Profile;
import org.example.taskmanager.exception.ExpectedEntityNotFoundException;
import org.example.taskmanager.repository.ProfileDAO;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class ProfileUtil {

    private final static Logger LOGGER = Logger.getLogger(ProfileUtil.class.getName());

    private final ModelMapper modelMapper;
    private final ProfileDAO profileDAO;

    public ProfileResponse tryToGetProfile(UUID id) {
        return convertToResponse(getEntity(id));
    }

    public Profile getEntity(UUID id) {
        return profileDAO.findById(id).orElseThrow(
                () -> new ExpectedEntityNotFoundException(String.format("Profile (%s) not found", id))
        );
    }

    /**
     * Getting profile entity by email
     *
     * @param email email
     * @return profile response model entity
     */
    private Profile getProfileEntityByEmail(String email) {
        return checkProfileForExistByEmail(email);
    }

    public UserDetailsService userDetailsService() {
        return this::getProfileEntityByEmail;
    }

    private Profile checkProfileForExistByEmail(String email) {
        var message = String.format("Profile with email \"%s\" non-exist", email);
        var profileEntity = profileDAO.findByEmail(email);
        if (profileEntity == null) {
            LOGGER.info(message);
            throw new ExpectedEntityNotFoundException(message);
        }
        return profileEntity;
    }

    public  <T> Profile convertToEntity(UUID id, T dto) {
        var profile = modelMapper.map(dto, Profile.class);
        if (profile.getId() == null) profile.setId(id);
        if (profile.getIsActive() == null) profile.setIsActive(true);
        return profile;
    }

    public ProfileResponse convertToResponse(Profile entity) {
        return modelMapper.map(entity, ProfileResponse.class);
    }
}

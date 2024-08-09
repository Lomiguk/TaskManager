package org.example.taskmanager.util;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.logging.Logger;

@Component
@AllArgsConstructor
public class ProfileAccessUtil {

    private static final Logger LOGGER = Logger.getLogger(ProfileAccessUtil.class.getName());

    private final JwtUtil jwtUtil;

    public void checkAuthorAuthorization(UUID id) {
        var authProfile = jwtUtil.getExistedAuthorizedProfileFromContext();

        if (!authProfile.getId().equals(id)) {
            LOGGER.info(String.format(
                    "AccessDeniedException: A Profile (%s) tried to influence information belonging to another Profile (%s)",
                    authProfile.getId(),
                    id
            ));
            throw new AccessDeniedException(String.format("Wrong authorized profile (%s)", authProfile.getId()));
        }
    }
}

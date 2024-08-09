package org.example.taskmanager.util;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProfileAccessUtil {

    private static final Logger LOGGER = Logger.getLogger(ProfileAccessUtil.class.getName());

    private final JwtUtil jwtUtil;

    public void checkAuthorAuthorization(Set<UUID> ids) {
        var authId = jwtUtil.getExistedAuthorizedProfileFromContext().getId();

        if (ids.stream().noneMatch(id -> id.equals(authId))) {
            LOGGER.info(String.format(
                    "AccessDeniedException: A Profile (%s) tried to influence information belonging to another Profile (%s)",
                    authId,
                    ids.stream().map(UUID::toString).collect(Collectors.joining(", "))
            ));
            throw new AccessDeniedException(String.format("Wrong authorized profile (%s)", authId));
        }
    }
}

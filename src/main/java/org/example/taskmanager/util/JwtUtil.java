package org.example.taskmanager.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.entity.Jwt;
import org.example.taskmanager.entity.Profile;
import org.example.taskmanager.exception.ExpectedEntityNotFoundException;
import org.example.taskmanager.repository.JwtDAO;
import org.example.taskmanager.repository.ProfileDAO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String NON_PROFILE_EXCEPTION_MESSAGE = "Authorization: Non existed profile";
    private static final String WRONG_SIGNATURE_MESSAGE = "A token with an incorrect signature was received";
    private static final String EXPIRED_JWT_MESSAGE = "Expired token received";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected error while validating JWT";
    private static final String NON_EXISTED_TOKEN_MESSAGE = "Non-existed token";
    private static final Logger LOGGER = Logger.getLogger(JwtUtil.class.getName());

    private final ProfileDAO profileDAO;
    private final JwtDAO jwtDAO;

    //TODO('get Authorized profile data')
    public Profile getExistedAuthorizedProfileFromContext() {
        return getExistedAuthorizedProfile(
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    public Profile getExistedAuthorizedProfile(Authentication auth) {
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof Profile) {
                var profileEntity = (Profile) auth.getPrincipal();
                return profileDAO.findByEmail(profileEntity.getEmail());
            }
        }
        throw new ExpectedEntityNotFoundException(NON_PROFILE_EXCEPTION_MESSAGE);
    }

    public boolean isTokenValid(String token, Key key) {
        if (jwtDAO.findByToken(token) == null) {
            LOGGER.info(NON_EXISTED_TOKEN_MESSAGE);
            throw new AccessDeniedException(NON_EXISTED_TOKEN_MESSAGE);
        }
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.log(Level.INFO, EXPIRED_JWT_MESSAGE, e);
            throw new AccessDeniedException(EXPIRED_JWT_MESSAGE);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, UNEXPECTED_ERROR_MESSAGE, e);
            throw new AccessDeniedException(UNEXPECTED_ERROR_MESSAGE);
        }
    }

    public Profile checkForExistedProfileProfileOrAccessDenied() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof Profile) {
                var profileEntity = (Profile) auth.getPrincipal();
                return profileDAO.findByEmail(profileEntity.getEmail());
            }
        }
        throw new AccessDeniedException(NON_PROFILE_EXCEPTION_MESSAGE);
    }

    public Jwt getEntity(UUID id) {
        return jwtDAO.findById(id).orElseThrow(
                () -> new ExpectedEntityNotFoundException(String.format("Jwt token (%s) not fount", id))
        );
    }
}

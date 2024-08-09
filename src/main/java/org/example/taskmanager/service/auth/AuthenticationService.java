package org.example.taskmanager.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.enums.JwtType;
import org.example.taskmanager.api.request.auth.SignInRequest;
import org.example.taskmanager.api.request.auth.SignUpRequest;
import org.example.taskmanager.api.response.JwtAuthenticationResponse;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.entity.Profile;
import org.example.taskmanager.exception.ExpectedEntityNotFoundException;
import org.example.taskmanager.repository.ProfileDAO;
import org.example.taskmanager.util.JwtUtil;
import org.example.taskmanager.util.PasswordUtil;
import org.example.taskmanager.util.ProfileUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final static Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());

    private final ProfileDAO profileDAO;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ProfileUtil profileUtil;
    private final JwtUtil jwtUtil;
    private final PasswordUtil passwordUtil;

    /**
     * Profile sign-up
     *
     * @param request user data
     * @return token
     */
    @Transactional
    public ProfileResponse signUp(SignUpRequest request) {
        return profileUtil.convertToResponse(profileDAO.save(new Profile(
                UUID.randomUUID(),
                request.getName(),
                request.getEmail(),
                passwordUtil.getEncodedPassword(request.getPassword()),
                true
        )));
    }

    /**
     * Profile authentication
     *
     * @param request user data
     * @return token
     */
    @Transactional
    public JwtAuthenticationResponse signIn(SignInRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var userDetails = profileUtil
                .userDetailsService()
                .loadUserByUsername(request.getEmail());
        var profileEntity = profileDAO.findByEmail(request.getEmail());
        var accessJwt = jwtService.registerToken(
                profileEntity.getId(),
                jwtService.generateAccessToken(userDetails),
                JwtType.ACCESS
        );
        var refreshJwt = jwtService.registerToken(
                profileEntity.getId(),
                jwtService.generateRefreshToken(userDetails),
                JwtType.REFRESH
        );

        return new JwtAuthenticationResponse(accessJwt, refreshJwt);
    }

    /**
     * Invalidate authorized profile token
     *
     * @return invalidation status
     */
    @Transactional
    public Boolean invalidateToken() {
        var profile = jwtUtil.checkForExistedProfileProfileOrAccessDenied();

        return profile != null && jwtService.deleteProfileToken(profile.getId());
    }

    /**
     * Refreshing profile's tokens
     *
     * @param refreshToken Refresh token
     * @return new access & refresh tokens
     */
    @Transactional
    public JwtAuthenticationResponse refreshTokens(String refreshToken) {
        jwtService.validateRefreshToken(refreshToken);
        var userLogin = jwtService.extractRefreshUserLogin(refreshToken);
        var userDetails = profileUtil
                .userDetailsService()
                .loadUserByUsername(userLogin);

        var profile = profileDAO.findByEmail(userLogin);

        // TODO('implement to checker class')
        checkProfileForExistByEmail(userLogin);

        jwtService.deleteProfileToken(profile.getId());
        var newAccessToken = jwtService.generateAccessToken(userDetails);
        var newRefreshToken = jwtService.generateRefreshToken(userDetails);
        jwtService.registerToken(profile.getId(), newAccessToken, JwtType.ACCESS);
        jwtService.registerToken(profile.getId(), newRefreshToken, JwtType.REFRESH);

        return new JwtAuthenticationResponse(newAccessToken, newRefreshToken);
    }

    private void checkProfileForExistByEmail(String emailLogin) {
        var message = String.format("Profile with email \"%s\" non-exist", emailLogin);
        var profileEntity = profileDAO.findByEmail(emailLogin);
        if (profileEntity == null) {
            LOGGER.info(message);
            throw new ExpectedEntityNotFoundException(message);
        }
    }
}

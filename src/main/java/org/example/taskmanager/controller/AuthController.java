package org.example.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.taskmanager.api.request.auth.SignInRequest;
import org.example.taskmanager.api.request.auth.SignUpRequest;
import org.example.taskmanager.api.response.JwtAuthenticationResponse;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.service.auth.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Creating new profile
     *
     * @param request       request with new profile data
     * @return Http response with created profile response model
     */
    @Operation(summary = "User sign-up")
    @PostMapping("/sign-up")
    public ResponseEntity<ProfileResponse> signUp(
            @Valid
            @RequestBody
            SignUpRequest request
    ) {
        return new ResponseEntity<>(
                authenticationService.signUp(request),
                HttpStatus.OK
        );
    }

    /**
     * Logout authorized profile
     *
     * @return true - if success
     */
    @Operation(summary = "User logout")
    @DeleteMapping("/logout")
    public ResponseEntity<Boolean> deleteToken() {
        return new ResponseEntity<>(
                authenticationService.invalidateToken(),
                HttpStatus.OK
        );
    }

    /**
     * Sign-in profile
     *
     * @param request       Profile credential
     * @return Http response with access & refresh token
     */
    @Operation(summary = "User sign-in")
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(
            @Valid
            @RequestBody
            SignInRequest request
    ) {
        return new ResponseEntity<>(
                authenticationService.signIn(request),
                HttpStatus.OK
        );
    }

    /**
     * Updating user access token
     *
     * @param refreshToken refresh token
     * @return Http response with new access & refresh token
     */
    @Operation(summary = "Updating user access token")
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody String refreshToken) {
        return new ResponseEntity<>(
                authenticationService.refreshTokens(refreshToken),
                HttpStatus.OK
        );
    }
}

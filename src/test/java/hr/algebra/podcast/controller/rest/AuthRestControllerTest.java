package hr.algebra.podcast.controller.rest;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import hr.algebra.podcast.dto.Dto;
import hr.algebra.podcast.entity.RefreshToken;
import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.security.JwtService;
import hr.algebra.podcast.service.AuthService;
import hr.algebra.podcast.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


class AuthRestControllerTest {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;
    private AuthService authService;
    private AuthRestController controller;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtService = mock(JwtService.class);
        refreshTokenService = mock(RefreshTokenService.class);
        authService = mock(AuthService.class);

        controller = new AuthRestController(
                authenticationManager,
                jwtService,
                refreshTokenService,
                authService
        );
    }

    /**
     * List of all tests in this file:
     * 1. testLoginSuccessful
     * 2. testRegisterSuccessful
     * 3. testRefreshSuccessful
     * 4. testLogoutSuccessful
     */

    @Test
    void testLoginSuccessful() {

        // arrange
        Dto.LoginRequest request =
                new Dto.LoginRequest("testuser", "password123");

        Authentication authentication = mock(Authentication.class);
        User user = mock(User.class);
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken("refresh-token");

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(user);
        when(jwtService.generateAccessToken(user))
                .thenReturn("access-token");
        when(refreshTokenService.createRefreshToken(user))
                .thenReturn(refreshToken);

        // act
        ResponseEntity<Dto.TokenResponse> response =
                controller.login(request);

        // assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateAccessToken(user);
        verify(refreshTokenService).createRefreshToken(user);
    }

    @Test
    void testRegisterSuccessful() {

        // arrange
        Dto.RegisterRequest request =
                new Dto.RegisterRequest(
                        "testuser",
                        "test@example.com",
                        "password123"
                );

        // act
        ResponseEntity<String> response =
                controller.register(request);

        // assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(
                "Registration successful.",
                response.getBody()
        );

        verify(authService).register(request);
    }

    @Test
    void testRefreshSuccessful() {

        // arrange
        Dto.RefreshTokenRequest request =
                new Dto.RefreshTokenRequest("old-refresh");

        User user = mock(User.class);

        RefreshToken oldRefresh = new RefreshToken();
        oldRefresh.setUser(user);
        oldRefresh.setToken("old-refresh");

        RefreshToken newRefresh = new RefreshToken();
        newRefresh.setUser(user);
        newRefresh.setToken("new-refresh");

        when(refreshTokenService.findByToken("old-refresh"))
                .thenReturn(Optional.of(oldRefresh));
        when(refreshTokenService.isValid(oldRefresh))
                .thenReturn(true);
        when(jwtService.generateAccessToken(user))
                .thenReturn("new-access");
        when(refreshTokenService.createRefreshToken(user))
                .thenReturn(newRefresh);

        // act
        ResponseEntity<Dto.TokenResponse> response =
                controller.refresh(request);

        // assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        verify(jwtService).generateAccessToken(user);
        verify(refreshTokenService).createRefreshToken(user);
    }

    @Test
    void testLogoutSuccessful() {

        // arrange
        Dto.RefreshTokenRequest request =
                new Dto.RefreshTokenRequest("refresh-token");

        User user = mock(User.class);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken("refresh-token");

        when(refreshTokenService.findByToken("refresh-token"))
                .thenReturn(Optional.of(refreshToken));

        // act
        ResponseEntity<String> response =
                controller.logout(request);

        // assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Logged out.", response.getBody());

        verify(refreshTokenService).revokeByUser(user);
    }


}
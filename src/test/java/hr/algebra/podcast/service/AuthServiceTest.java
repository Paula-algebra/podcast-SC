package hr.algebra.podcast.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import hr.algebra.podcast.dto.Dto;
import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;



class AuthServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authService = new AuthService(userRepository, passwordEncoder);
    }

    @Test
    void successfulRegister() {

        // arrange
        Dto.RegisterRequest request =
                new Dto.RegisterRequest(
                        "testuser",
                        "test@example.com",
                        "password123"
                );

        when(userRepository.existsByUsername("testuser"))
                .thenReturn(false);
        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        // act
        authService.register(request);

        // assert
        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("USER", savedUser.getRole().name());
        assertTrue(savedUser.isEnabled());
    }

    @Test
    void testDuplicateUsername() {

        // arrange
        Dto.RegisterRequest request =
                new Dto.RegisterRequest(
                        "testuser",
                        "test@example.com",
                        "password123"
                );

        when(userRepository.existsByUsername("testuser"))
                .thenReturn(true);

        // act
        // assert
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.register(request)
                );
        assertEquals(
                "Username already taken.",
                exception.getMessage()
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDuplicateEmail() {

        // Arrange
        Dto.RegisterRequest request =
                new Dto.RegisterRequest(
                        "testuser",
                        "test@example.com",
                        "password123"
                );

        when(userRepository.existsByUsername("testuser"))
                .thenReturn(false);

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        // Act
        // Assert
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.register(request)
                );
        assertEquals(
                "Email already registered.",
                exception.getMessage()
        );
        verify(userRepository, never()).save(any());
    }
}
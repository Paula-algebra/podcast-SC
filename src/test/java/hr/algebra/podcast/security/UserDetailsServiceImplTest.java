package hr.algebra.podcast.security;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        service = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void testLoadUserByUsername() {

        // arrange
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        // act
        UserDetails result = service.loadUserByUsername("testuser");

        // assert
        assertSame(user, result);
    }

    @Test
    void testUserNotFound() {

        // arrange
        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing")
        );
    }
}
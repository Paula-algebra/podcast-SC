package hr.algebra.podcast.entity;


import static org.junit.jupiter.api.Assertions.*;

import hr.algebra.podcast.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;


class UserTest {

    @Test
    void testGettersSettersAndUserDetailsMethods() {

        // arrange
        User user = new User();

        LocalDateTime registeredAt =
                LocalDateTime.of(2026, 1, 1, 10, 0);

        // act
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
        user.setEnabled(false);
        user.setRegisteredAt(registeredAt);

        Collection<? extends GrantedAuthority> authorities =
                user.getAuthorities();

        // assert
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Role.USER, user.getRole());
        assertFalse(user.isEnabled());
        assertEquals(registeredAt, user.getRegisteredAt());

        assertEquals(1, authorities.size());
        assertEquals(
                "ROLE_USER",
                authorities.iterator().next().getAuthority()
        );

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testOnCreateSetsRegisteredAt() {

        // arrange
        User user = new User();

        // act
        user.onCreate();

        // assert
        assertNotNull(user.getRegisteredAt());
    }
}
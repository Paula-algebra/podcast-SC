package hr.algebra.podcast.security;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;


class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                "PodCastSecretKey2026ChangeThisInProductionToSomethingMuchLonger"
        );

        ReflectionTestUtils.setField(
                jwtService,
                "accessExpiryMs",
                900000L
        );
    }

    @Test
    void testGenerateAndValidateToken() {

        // arrange
        UserDetails user = mock(UserDetails.class);
        when(user.getUsername()).thenReturn("testuser");
        doReturn(List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        )).when(user).getAuthorities();

        // act
        String token = jwtService.generateAccessToken(user);

        // assert
        assertNotNull(token);
        assertEquals("testuser", jwtService.extractUsername(token));
        assertTrue(jwtService.isValid(token, user));
        assertNull(jwtService.extractUsername("invalid-token"));
    }
}
package hr.algebra.podcast.config;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import hr.algebra.podcast.entity.Episode;
import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.repository.EpisodeRepository;
import hr.algebra.podcast.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;


class DataInitializerTest {

    private UserRepository userRepository;
    private EpisodeRepository episodeRepository;
    private PasswordEncoder passwordEncoder;
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        episodeRepository = mock(EpisodeRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        dataInitializer = new DataInitializer(
                userRepository,
                episodeRepository,
                passwordEncoder
        );
    }

    @Test
    void testDataAlreadyExists() {

        // arrange
        ApplicationArguments args = mock(ApplicationArguments.class);
        when(userRepository.count()).thenReturn(1L);

        // act
        dataInitializer.run(args);

        // assert
        verify(userRepository).count();
        verify(userRepository, never()).save(any(User.class));
        verify(episodeRepository, never()).save(any(Episode.class));
    }

    @Test
    void testDataIsEmpty() {

        // arrange
        ApplicationArguments args = mock(ApplicationArguments.class);
        when(userRepository.count()).thenReturn(0L);

        when(passwordEncoder.encode(nullable(CharSequence.class)))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(episodeRepository.save(any(Episode.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act
        dataInitializer.run(args);

        // assert
        verify(userRepository).count();
        verify(userRepository, times(2)).save(any(User.class));
        verify(passwordEncoder, times(2))
                .encode(nullable(CharSequence.class));
        verify(episodeRepository, times(10))
                .save(any(Episode.class));
    }
}
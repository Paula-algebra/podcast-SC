package hr.algebra.podcast.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hr.algebra.podcast.dto.EpisodeDto;
import hr.algebra.podcast.entity.Episode;
import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.enums.ListeningStatus;
import hr.algebra.podcast.enums.PodcastCategory;
import hr.algebra.podcast.repository.EpisodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


class EpisodeServiceTest {

    private EpisodeRepository episodeRepository;
    private EpisodeService episodeService;

    @BeforeEach
    void setUp() {
        episodeRepository = mock(EpisodeRepository.class);
        episodeService = new EpisodeService(episodeRepository);
    }

    @Test
    void testFindAll() {

        // arrange
        Episode episode = mock(Episode.class);
        EpisodeDto dto = mock(EpisodeDto.class);

        when(episodeRepository.findAllByOrderByStatusAscReleaseDateDescShowNameAsc())
                .thenReturn(List.of(episode));

        try (MockedStatic<EpisodeDto> mockedDto = mockStatic(EpisodeDto.class)) {

            mockedDto.when(() -> EpisodeDto.from(episode))
                    .thenReturn(dto);

            // act
            List<EpisodeDto> result = episodeService.findAll();

            // assert
            assertEquals(1, result.size());
            assertSame(dto, result.getFirst());
        }
    }

    @Test
    void testFindByIdSuccessful() {

        // arrange
        Episode episode = mock(Episode.class);
        EpisodeDto dto = mock(EpisodeDto.class);

        when(episodeRepository.findById(1L))
                .thenReturn(Optional.of(episode));

        try (MockedStatic<EpisodeDto> mockedDto = mockStatic(EpisodeDto.class)) {

            mockedDto.when(() -> EpisodeDto.from(episode))
                    .thenReturn(dto);

            // act
            EpisodeDto result = episodeService.findById(1L);

            // assert
            assertSame(dto, result);
        }
    }

    @Test
    void testFindByIdNotFound() {

        // arrange
        when(episodeRepository.findById(1L))
                .thenReturn(Optional.empty());

        // act & assert
        NoSuchElementException exception =
                assertThrows(
                        NoSuchElementException.class,
                        () -> episodeService.findById(1L)
                );

        assertEquals(
                "Episode not found: 1",
                exception.getMessage()
        );
    }

    @Test
    void testSearch() {

        // arrange
        Episode episode = mock(Episode.class);
        EpisodeDto dto = mock(EpisodeDto.class);

        when(episodeRepository.search(
                "test",
                PodcastCategory.TECHNOLOGY,
                ListeningStatus.QUEUED,
                true
        )).thenReturn(List.of(episode));

        try (MockedStatic<EpisodeDto> mockedDto = mockStatic(EpisodeDto.class)) {

            mockedDto.when(() -> EpisodeDto.from(episode))
                    .thenReturn(dto);

            // act
            List<EpisodeDto> result = episodeService.search(
                    "test",
                    PodcastCategory.TECHNOLOGY,
                    ListeningStatus.QUEUED,
                    true
            );

            // assert
            assertEquals(1, result.size());
            assertSame(dto, result.getFirst());
        }
    }

    @Test
    void testSearchBlankQuery() {

        // arrange
        when(episodeRepository.search(
                null,
                null,
                null,
                false
        )).thenReturn(List.of());

        // act
        List<EpisodeDto> result =
                episodeService.search("   ", null, null, false);

        // assert
        assertTrue(result.isEmpty());

        verify(episodeRepository)
                .search(null, null, null, false);
    }

    @Test
    void testCreate() {

        // arrange
        EpisodeDto dto = mock(EpisodeDto.class);
        EpisodeDto resultDto = mock(EpisodeDto.class);
        User creator = mock(User.class);

        when(episodeRepository.save(any(Episode.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        try (MockedStatic<EpisodeDto> mockedDto = mockStatic(EpisodeDto.class)) {

            mockedDto.when(() -> EpisodeDto.from(any(Episode.class)))
                    .thenReturn(resultDto);

            // act
            EpisodeDto result = episodeService.create(dto, creator);

            // assert
            assertSame(resultDto, result);

            verify(dto).applyTo(any(Episode.class));
            verify(episodeRepository).save(any(Episode.class));
        }
    }

    @Test
    void testUpdateSuccessful() {

        // arrange
        Episode episode = mock(Episode.class);
        EpisodeDto dto = mock(EpisodeDto.class);
        EpisodeDto resultDto = mock(EpisodeDto.class);

        when(episodeRepository.findById(1L))
                .thenReturn(Optional.of(episode));

        when(episodeRepository.save(episode))
                .thenReturn(episode);

        try (MockedStatic<EpisodeDto> mockedDto = mockStatic(EpisodeDto.class)) {

            mockedDto.when(() -> EpisodeDto.from(episode))
                    .thenReturn(resultDto);

            // act
            EpisodeDto result = episodeService.update(1L, dto);

            // assert
            assertSame(resultDto, result);

            verify(dto).applyTo(episode);
            verify(episodeRepository).save(episode);
        }
    }

    @Test
    void testUpdateNotFound() {

        // arrange
        EpisodeDto dto = mock(EpisodeDto.class);

        when(episodeRepository.findById(1L))
                .thenReturn(Optional.empty());

        // act & assert
        NoSuchElementException exception =
                assertThrows(
                        NoSuchElementException.class,
                        () -> episodeService.update(1L, dto)
                );

        assertEquals(
                "Episode not found: 1",
                exception.getMessage()
        );

        verify(episodeRepository, never()).save(any());
    }

    @Test
    void testDeleteSuccessful() {

        // arrange
        when(episodeRepository.existsById(1L))
                .thenReturn(true);

        // act
        episodeService.delete(1L);

        // assert
        verify(episodeRepository).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {

        // arrange
        when(episodeRepository.existsById(1L))
                .thenReturn(false);

        // act & assert
        NoSuchElementException exception =
                assertThrows(
                        NoSuchElementException.class,
                        () -> episodeService.delete(1L)
                );

        assertEquals(
                "Episode not found: 1",
                exception.getMessage()
        );

        verify(episodeRepository, never()).deleteById(anyLong());
    }
}
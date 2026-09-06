package hr.algebra.podcast.controller.rest;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import hr.algebra.podcast.dto.EpisodeDto;
import hr.algebra.podcast.dto.EpisodeDto;
import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.service.EpisodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;


class EpisodeRestControllerTest {

    private EpisodeService episodeService;
    private EpisodeRestController controller;

    @BeforeEach
    void setUp() {
        episodeService = mock(EpisodeService.class);
        controller = new EpisodeRestController(episodeService);
    }

    /**
     * Tests:
     * testGetAll
     * testGetById
     * testGetByIdNotFound
     * testCreate
     * testUpdate
     * testDelete
     *
     * */

    @Test
    void testGetAll() {

        // arrange
        EpisodeDto dto = mock(EpisodeDto.class);
        List<EpisodeDto> episodes = List.of(dto);

        when(episodeService.findAll())
                .thenReturn(episodes);

        // act
        ResponseEntity<List<EpisodeDto>> response =
                controller.getAll();

        // assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(episodes, response.getBody());

        verify(episodeService).findAll();
    }

    @Test
    void testGetById() {

        // arrange
        EpisodeDto dto = mock(EpisodeDto.class);

        when(episodeService.findById(1L))
                .thenReturn(dto);

        // act
        ResponseEntity<EpisodeDto> response =
                controller.getById(1L);

        // assert
        assertEquals(200, response.getStatusCode().value());
        assertSame(dto, response.getBody());

        verify(episodeService).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {

        // arrange
        when(episodeService.findById(999L))
                .thenThrow(new NoSuchElementException());

        // act
        ResponseEntity<EpisodeDto> response =
                controller.getById(999L);

        // assert
        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testCreate() {

        // arrange
        EpisodeDto requestDto = mock(EpisodeDto.class);
        EpisodeDto createdDto = mock(EpisodeDto.class);
        User user = mock(User.class);

        when(episodeService.create(requestDto, user))
                .thenReturn(createdDto);

        // act
        ResponseEntity<EpisodeDto> response =
                controller.create(requestDto, user);

        // assert
        assertEquals(201, response.getStatusCode().value());
        assertSame(createdDto, response.getBody());

        verify(episodeService).create(requestDto, user);
    }

    @Test
    void testUpdate() {

        // arrange
        EpisodeDto requestDto = mock(EpisodeDto.class);
        EpisodeDto updatedDto = mock(EpisodeDto.class);

        when(episodeService.update(1L, requestDto))
                .thenReturn(updatedDto);

        // act
        ResponseEntity<EpisodeDto> response =
                controller.update(1L, requestDto);

        // assert
        assertEquals(200, response.getStatusCode().value());
        assertSame(updatedDto, response.getBody());

        verify(episodeService).update(1L, requestDto);
    }

    @Test
    void testDelete() {

        // arrange

        // act
        ResponseEntity<Void> response =
                controller.delete(2L);

        // assert
        assertEquals(204, response.getStatusCode().value());

        verify(episodeService).delete(2L);
    }

}
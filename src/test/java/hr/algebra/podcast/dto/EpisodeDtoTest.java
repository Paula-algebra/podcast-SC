package hr.algebra.podcast.dto;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import hr.algebra.podcast.entity.Episode;
import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.enums.ListeningContext;
import hr.algebra.podcast.enums.ListeningStatus;
import hr.algebra.podcast.enums.PlaybackSpeed;
import hr.algebra.podcast.enums.PodcastCategory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;


class EpisodeDtoTest {

    @Test
    void testFromEpisode() {

        // arrange
        Episode episode = mock(Episode.class);
        User user = mock(User.class);

        LocalDate releaseDate = LocalDate.of(2026, 1, 1);
        LocalDate listenedDate = LocalDate.of(2026, 1, 2);
        LocalDate addedDate = LocalDate.of(2025, 12, 30);

        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 2, 11, 0);

        when(episode.getId()).thenReturn(1L);
        when(episode.getTitle()).thenReturn("Test Episode");
        when(episode.getShowName()).thenReturn("Test Show");
        when(episode.getHosts()).thenReturn("Host");
        when(episode.getGuests()).thenReturn("Guest");
        when(episode.getNetwork()).thenReturn("Spotify");
        when(episode.getEpisodeNumber()).thenReturn("E1");
        when(episode.getSeasonNumber()).thenReturn(1);

        when(episode.getCategory())
                .thenReturn(PodcastCategory.TECHNOLOGY);

        when(episode.getStatus())
                .thenReturn(ListeningStatus.QUEUED);

        when(episode.getListeningContext())
                .thenReturn(ListeningContext.COMMUTE);

        when(episode.getPlaybackSpeed())
                .thenReturn(PlaybackSpeed.SPEED_1_0X);

        when(episode.getDurationMinutes()).thenReturn(60);
        when(episode.getMinutesListened()).thenReturn(30);
        when(episode.getRating()).thenReturn(5);
        when(episode.getContentQuality()).thenReturn(9);
        when(episode.getAudioQuality()).thenReturn(8);
        when(episode.getHostChemistry()).thenReturn(9);
        when(episode.getRewatchValue()).thenReturn(7);

        when(episode.isExplicitContent()).thenReturn(true);
        when(episode.isSubscribed()).thenReturn(true);
        when(episode.isBookmarkedQuote()).thenReturn(false);
        when(episode.isRecommendToFriend()).thenReturn(true);

        when(episode.getReleaseDate()).thenReturn(releaseDate);
        when(episode.getListenedDate()).thenReturn(listenedDate);
        when(episode.getAddedDate()).thenReturn(addedDate);

        when(episode.getMoodTags()).thenReturn("fun");
        when(episode.getMainTopic()).thenReturn("technology");
        when(episode.getMemorableQuote()).thenReturn("quote");
        when(episode.getKeyTakeaway()).thenReturn("takeaway");
        when(episode.getReview()).thenReturn("review");
        when(episode.getPersonalNotes()).thenReturn("notes");

        when(episode.getAddedBy()).thenReturn(user);
        when(user.getUsername()).thenReturn("admin");

        when(episode.getCreatedAt()).thenReturn(createdAt);
        when(episode.getUpdatedAt()).thenReturn(updatedAt);

        // act
        EpisodeDto dto = EpisodeDto.from(episode);

        // assert
        assertEquals(1L, dto.id());
        assertEquals("Test Episode", dto.title());
        assertEquals("Test Show", dto.showName());
        assertEquals("Host", dto.hosts());
        assertEquals("Guest", dto.guests());
        assertEquals("Spotify", dto.network());
        assertEquals("E1", dto.episodeNumber());
        assertEquals(1, dto.seasonNumber());
        assertEquals(PodcastCategory.TECHNOLOGY, dto.category());
        assertEquals(ListeningStatus.QUEUED, dto.status());
        assertEquals(ListeningContext.COMMUTE, dto.listeningContext());
        assertEquals(PlaybackSpeed.SPEED_1_0X, dto.playbackSpeed());
        assertEquals(60, dto.durationMinutes());
        assertEquals(30, dto.minutesListened());
        assertEquals(5, dto.rating());
        assertEquals(9, dto.contentQuality());
        assertEquals(8, dto.audioQuality());
        assertEquals(9, dto.hostChemistry());
        assertEquals(7, dto.rewatchValue());
        assertTrue(dto.explicitContent());
        assertTrue(dto.subscribed());
        assertFalse(dto.bookmarkedQuote());
        assertTrue(dto.recommendToFriend());
        assertEquals(releaseDate, dto.releaseDate());
        assertEquals(listenedDate, dto.listenedDate());
        assertEquals(addedDate, dto.addedDate());
        assertEquals("fun", dto.moodTags());
        assertEquals("technology", dto.mainTopic());
        assertEquals("quote", dto.memorableQuote());
        assertEquals("takeaway", dto.keyTakeaway());
        assertEquals("review", dto.review());
        assertEquals("notes", dto.personalNotes());
        assertEquals("admin", dto.addedBy());
        assertEquals(createdAt, dto.createdAt());
        assertEquals(updatedAt, dto.updatedAt());
    }

    @Test
    void testFromEpisodeWithoutAddedBy() {

        // arrange
        Episode episode = mock(Episode.class);

        when(episode.getAddedBy()).thenReturn(null);

        // act
        EpisodeDto dto = EpisodeDto.from(episode);

        // assert
        assertNull(dto.addedBy());
    }

    @Test
    void testApplyToEpisode() {

        // arrange
        LocalDate releaseDate = LocalDate.of(2026, 1, 1);
        LocalDate listenedDate = LocalDate.of(2026, 1, 2);
        LocalDate addedDate = LocalDate.of(2025, 12, 30);

        EpisodeDto dto = new EpisodeDto(
                1L,
                "Test Episode",
                "Test Show",
                "Host",
                "Guest",
                "Spotify",
                "E1",
                1,
                PodcastCategory.TECHNOLOGY,
                ListeningStatus.QUEUED,
                ListeningContext.COMMUTE,
                PlaybackSpeed.SPEED_1_0X,
                60,
                30,
                5,
                9,
                8,
                9,
                7,
                true,
                true,
                false,
                true,
                releaseDate,
                listenedDate,
                addedDate,
                "fun",
                "technology",
                "quote",
                "takeaway",
                "review",
                "notes",
                "admin",
                null,
                null
        );

        Episode episode = mock(Episode.class);

        // act
        dto.applyTo(episode);

        // assert
        verify(episode).setTitle("Test Episode");
        verify(episode).setShowName("Test Show");
        verify(episode).setHosts("Host");
        verify(episode).setGuests("Guest");
        verify(episode).setNetwork("Spotify");
        verify(episode).setEpisodeNumber("E1");
        verify(episode).setSeasonNumber(1);
        verify(episode).setCategory(PodcastCategory.TECHNOLOGY);
        verify(episode).setStatus(ListeningStatus.QUEUED);
        verify(episode).setListeningContext(ListeningContext.COMMUTE);
        verify(episode).setPlaybackSpeed(PlaybackSpeed.SPEED_1_0X);
        verify(episode).setDurationMinutes(60);
        verify(episode).setMinutesListened(30);
        verify(episode).setRating(5);
        verify(episode).setContentQuality(9);
        verify(episode).setAudioQuality(8);
        verify(episode).setHostChemistry(9);
        verify(episode).setRewatchValue(7);
        verify(episode).setExplicitContent(true);
        verify(episode).setSubscribed(true);
        verify(episode).setBookmarkedQuote(false);
        verify(episode).setRecommendToFriend(true);
        verify(episode).setReleaseDate(releaseDate);
        verify(episode).setListenedDate(listenedDate);
        verify(episode).setAddedDate(addedDate);
        verify(episode).setMoodTags("fun");
        verify(episode).setMainTopic("technology");
        verify(episode).setMemorableQuote("quote");
        verify(episode).setKeyTakeaway("takeaway");
        verify(episode).setReview("review");
        verify(episode).setPersonalNotes("notes");
    }
}
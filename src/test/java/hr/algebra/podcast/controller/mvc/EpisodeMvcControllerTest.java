package hr.algebra.podcast.controller.mvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import hr.algebra.podcast.dto.EpisodeDto;
import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.service.EpisodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class EpisodeMvcControllerTest {

    private EpisodeService episodeService;
    private EpisodeMvcController controller;

    @BeforeEach
    void setUp() {
        episodeService = mock(EpisodeService.class);
        controller = new EpisodeMvcController(episodeService);
    }

    /** list of all tests in this file because there's 12 of them:
     * 1. testEpisodeList - tests loading all episodes without any filters applied
     * 2. testEpisodeListWithSearch - tests loading episodes with filter applied
     * 3. testEpisodeDetailsSuccessful - tests what happens when deatils page of an existing episode is opened
     * 4. testEpisodeNotFound - tests what happens when episode that doesn't exist is opened
     * 5. testNewEpisodeForm
     * 6. testCreateWithValidationErrors
     * 7. testCreateSuccessful
     * 8. testEditFormFound
     * 9. testEditFormNotFound - tests what happens when someone tries to open edit form of an episode that doesn't exist, e.g. /episodes/edit/999
     * 10. testUpdateWithValidationErrors
     * 11. testUpdateSuccessful
     * 12. testDelete
     **/

    @Test
    void testEpisodeList() {

        // arrange
        Model model = mock(Model.class);

        when(episodeService.findAll())
                .thenReturn(List.of());

        // act
        String result = controller.list(
                null,
                null,
                null,
                false,
                model
        );

        // assert
        assertEquals("episodes/list", result);
        verify(episodeService).findAll();
        verify(episodeService, never())
                .search(any(), any(), any(), anyBoolean());
    }

    @Test
    void testEpisodeListWithSearch() {

        // arrange
        Model model = mock(Model.class);

        when(episodeService.search(
                "test",
                null,
                null,
                false
        )).thenReturn(List.of());

        // act
        String result = controller.list(
                "test",
                null,
                null,
                false,
                model
        );

        // assert
        assertEquals("episodes/list", result);
        verify(episodeService)
                .search("test", null, null, false);
        verify(episodeService, never()).findAll();
    }

    @Test
    void testEpisodeDetailsSuccessful() {

        // arrange
        Model model = mock(Model.class);
        EpisodeDto dto = mock(EpisodeDto.class);

        when(episodeService.findById(1L))
                .thenReturn(dto);

        // act
        String result = controller.detail(1L, model);

        // assert
        assertEquals("episodes/detail", result);

        verify(model).addAttribute("episode", dto);
    }

    @Test
    void testEpisodeNotFound() {

        // arrange
        Model model = mock(Model.class);
        when(episodeService.findById(999L))
                .thenThrow(new NoSuchElementException());

        // act
        String result = controller.detail(999L, model);

        // assert
        assertEquals("redirect:/episodes", result);
    }

    @Test
    void testNewEpisodeForm() {

        // arrange
        Model model = mock(Model.class);

        // act
        String result = controller.newForm(model);

        // assert
        assertEquals("episodes/form", result);
        verify(model).addAttribute(
                eq("episode"),
                any(EpisodeDto.class)
        );
        verify(model).addAttribute("editMode", false);
    }

    @Test
    void testCreateWithValidationErrors() {

        // arrange
        EpisodeDto dto = mock(EpisodeDto.class);
        BindingResult bindingResult = mock(BindingResult.class);
        User user = mock(User.class);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        when(bindingResult.hasErrors())
                .thenReturn(true);

        // act
        String result = controller.create(
                dto,
                bindingResult,
                user,
                model,
                redirectAttributes
        );

        // assert
        assertEquals("episodes/form", result);
        verify(episodeService, never())
                .create(any(), any());
        verify(model).addAttribute("editMode", false);
    }

    @Test
    void testCreateSuccessful() {

        // arrange
        EpisodeDto dto = mock(EpisodeDto.class);
        BindingResult bindingResult = mock(BindingResult.class);
        User user = mock(User.class);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        when(bindingResult.hasErrors())
                .thenReturn(false);

        // act
        String result = controller.create(
                dto,
                bindingResult,
                user,
                model,
                redirectAttributes
        );

        // assert
        assertEquals("redirect:/episodes", result);
        verify(episodeService).create(dto, user);
        verify(redirectAttributes).addFlashAttribute(
                "successMessage",
                "Episode added to your queue."
        );
    }

    @Test
    void testEditFormSuccessful() {

        // arrange
        Model model = mock(Model.class);
        EpisodeDto dto = mock(EpisodeDto.class);

        when(episodeService.findById(1L))
                .thenReturn(dto);

        // act
        String result = controller.editForm(1L, model);

        // assert
        assertEquals("episodes/form", result);
        verify(model).addAttribute("episode", dto);
        verify(model).addAttribute("editMode", true);
    }

    @Test
    void testEditFormNotFound() {

        // arrange
        Model model = mock(Model.class);

        when(episodeService.findById(999L))
                .thenThrow(new NoSuchElementException());

        // act
        String result = controller.editForm(999L, model);

        // assert
        assertEquals("redirect:/episodes", result);
    }

    @Test
    void testUpdateWithValidationErrors() {

        // arrange
        EpisodeDto dto = mock(EpisodeDto.class);
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        when(bindingResult.hasErrors())
                .thenReturn(true);

        // act
        String result = controller.update(
                1L,
                dto,
                bindingResult,
                model,
                redirectAttributes
        );

        // assert
        assertEquals("episodes/form", result);
        verify(episodeService, never())
                .update(anyLong(), any());
        verify(model).addAttribute("editMode", true);
    }

    @Test
    void testUpdateSuccessful() {

        // arrange
        EpisodeDto dto = mock(EpisodeDto.class);
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        when(bindingResult.hasErrors())
                .thenReturn(false);

        // act
        String result = controller.update(
                1L,
                dto,
                bindingResult,
                model,
                redirectAttributes
        );

        // assert
        assertEquals("redirect:/episodes", result);

        verify(episodeService).update(1L, dto);
        verify(redirectAttributes).addFlashAttribute(
                "successMessage",
                "Episode updated."
        );
    }

    @Test
    void testDelete() {

        // arrange
        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        // act
        String result = controller.delete(
                2L,
                redirectAttributes
        );

        // assert
        assertEquals("redirect:/episodes", result);
        verify(episodeService).delete(2L);
        verify(redirectAttributes).addFlashAttribute(
                "successMessage",
                "Episode removed."
        );
    }
}
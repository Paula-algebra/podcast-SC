package hr.algebra.podcast.controller.mvc;

import org.springframework.web.bind.annotation.*;
import hr.algebra.podcast.dto.EpisodeDto;
import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.enums.ListeningContext;
import hr.algebra.podcast.enums.ListeningStatus;
import hr.algebra.podcast.enums.PlaybackSpeed;
import hr.algebra.podcast.enums.PodcastCategory;
import hr.algebra.podcast.service.EpisodeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/episodes")
public class EpisodeMvcController {

    private final EpisodeService episodeService;
    private static final String EPISODE_ATTRIBUTE = "episode";
    private static final String MESSAGE_SUCCESS = "successMessage";
    private static final String EPISODES_FORM = "episodes/form";
    private static final String EDIT_MODE = "editMode";
    private static final String REDIRECT = "redirect:/episodes";

    public EpisodeMvcController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) PodcastCategory category,
        @RequestParam(required = false) ListeningStatus status,
        @RequestParam(defaultValue = "false") boolean subscribedOnly,
        Model model
    ) {
        boolean searching = query != null || category != null || status != null || subscribedOnly;
        model.addAttribute("episodes", searching
            ? episodeService.search(query, category, status, subscribedOnly)
            : episodeService.findAll());
        addEnumsToModel(model);
        model.addAttribute("query", query);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("subscribedOnly", subscribedOnly);
        return "episodes/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try {
            model.addAttribute(EPISODE_ATTRIBUTE, episodeService.findById(id));
            return "episodes/detail";
        } catch (NoSuchElementException _) {
            return REDIRECT;
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newForm(Model model) {
        model.addAttribute(EPISODE_ATTRIBUTE, new EpisodeDto(
            null, "", "", "", "", "", "", null,
                PodcastCategory.TECHNOLOGY, ListeningStatus.QUEUED, null, null,
            null, null, null, null, null, null, null,
            false, false, false, false,
            null, null, null,
            "", "", "", "", "", "",
            null, null, null
        ));
        addEnumsToModel(model);
        model.addAttribute(EDIT_MODE, false);
        return EPISODES_FORM;
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(
        @Valid @ModelAttribute("episode") EpisodeDto dto,
        BindingResult result,
        @AuthenticationPrincipal User currentUser,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addEnumsToModel(model);
            model.addAttribute(EDIT_MODE, false);
            return EPISODES_FORM;
        }
        episodeService.create(dto, currentUser);
        redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS, "Episode added to your queue.");
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute(EPISODE_ATTRIBUTE, episodeService.findById(id));
            addEnumsToModel(model);
            model.addAttribute(EDIT_MODE, true);
            return EPISODES_FORM;
        } catch (NoSuchElementException _) {
            return REDIRECT;
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("episode") EpisodeDto dto,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addEnumsToModel(model);
            model.addAttribute(EDIT_MODE, true);
            return EPISODES_FORM;
        }
        episodeService.update(id, dto);
        redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS, "Episode updated.");
        return REDIRECT;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        episodeService.delete(id);
        redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS, "Episode removed.");
        return REDIRECT;
    }

    private void addEnumsToModel(Model model) {
        model.addAttribute("categories", PodcastCategory.values());
        model.addAttribute("statuses", ListeningStatus.values());
        model.addAttribute("contexts", ListeningContext.values());
        model.addAttribute("speeds", PlaybackSpeed.values());
    }
}

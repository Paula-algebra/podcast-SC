package hr.algebra.podcast.controller.mvc;

import static org.mockito.Mockito.*;
import hr.algebra.podcast.dto.Dto;
import hr.algebra.podcast.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AuthMvcControllerTest {

    private AuthService authService;
    private AuthMvcController controller;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        controller = new AuthMvcController(authService);
    }

    @Test
    void testLoginPage() {

        // act
        String result = controller.loginPage();
        // assert
        assertEquals("auth/login", result);
    }

    @Test
    void testRegisterPage() {

        // arrange
        Model model = mock(Model.class);
        // act
        String result = controller.registerPage(model);
        // assert
        assertEquals("auth/register", result);
        verify(model).addAttribute(
                eq("registerRequest"),
                any(Dto.RegisterRequest.class)
        );
    }

    @Test
    void testRegisterWithValidationErrors() {

        // arrange
        Dto.RegisterRequest request =
                new Dto.RegisterRequest(
                        "testuser",
                        "test@example.com",
                        "password123"
                );

        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Model model = mock(Model.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        // act
        String result = controller.register(
                request,
                bindingResult,
                redirectAttributes,
                model
        );

        // assert
        assertEquals("auth/register", result);
        verify(authService, never()).register(any());
    }

    @Test
    void testRegisterSuccessful() {

        // arrange
        Dto.RegisterRequest request =
                new Dto.RegisterRequest(
                        "testuser",
                        "test@example.com",
                        "password123"
                );

        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Model model = mock(Model.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        // act
        String result = controller.register(
                request,
                bindingResult,
                redirectAttributes,
                model
        );

        // assert
        assertEquals("redirect:/auth/login", result);
        verify(authService).register(request);
        verify(redirectAttributes).addFlashAttribute(
                "successMessage",
                "Account created! Please log in."
        );
    }

    @Test
    void testRegisterFailure() {

        // arrange
        Dto.RegisterRequest request =
                new Dto.RegisterRequest(
                        "testuser",
                        "test@example.com",
                        "password123"
                );

        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Model model = mock(Model.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        doThrow(new IllegalArgumentException("Username already taken."))
                .when(authService)
                .register(request);

        // act
        String result = controller.register(
                request,
                bindingResult,
                redirectAttributes,
                model
        );

        // assert
        assertEquals("auth/register", result);
        verify(model).addAttribute(
                "errorMessage",
                "Username already taken."
        );
    }
}
package hr.algebra.podcast.security;

import hr.algebra.podcast.config.DataInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthSecurityIntegrationTest {

    @MockitoBean
    private DataInitializer dataInitializer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testUnauthenticatedAccessReturns403() throws Exception {

        // act
        // assert
        mockMvc.perform(get("/api/episodes"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUserCannotDeleteEpisode() throws Exception {

        // act
        // assert
        mockMvc.perform(delete("/api/episodes/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAdminCanAccessEpisodes() throws Exception {

        // act
        var result = mockMvc.perform(
                get("/api/episodes")
                        .with(user("admin").roles("ADMIN"))
        );
        // assert
        result.andExpect(status().isOk());
    }
}
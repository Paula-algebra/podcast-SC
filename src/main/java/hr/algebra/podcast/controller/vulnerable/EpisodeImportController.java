package hr.algebra.podcast.controller.vulnerable;

import org.springframework.web.bind.annotation.*;
import hr.algebra.podcast.service.SafeUrlValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EpisodeImportController {

    private final SafeUrlValidator urlValidator;

    private final RestClient http = RestClient.builder()
            .requestFactory(new JdkClientHttpRequestFactory(
                    HttpClient.newBuilder()
                            .followRedirects(HttpClient.Redirect.NEVER)
                            .build()))
            .build();

    public EpisodeImportController(SafeUrlValidator urlValidator) {
        this.urlValidator = urlValidator;
    }

    @PostMapping("/episodes/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> preview(@RequestParam String url) {
        URI safeUri;
        try {
            safeUri = urlValidator.validateOrThrow(url);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }

        try {
            String body = http.get()
                    .uri(safeUri)
                    .retrieve()
                    .body(String.class);

            Map<String, Object> result = new HashMap<>();
            result.put("body", body == null ? "" : body);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.status(502)
                    .body(Map.of("error", "Upstream fetch failed"));
        }
    }
}
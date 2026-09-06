package hr.algebra.podcast.config;

import hr.algebra.podcast.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private static final String API_EPISODES = "/api/episodes/**";
    private static final String ROLE_ADMIN = "ADMIN";

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) {
        http
            .securityMatcher("/api/**")
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, API_EPISODES).hasAnyRole("USER", ROLE_ADMIN)
                .requestMatchers(HttpMethod.POST, API_EPISODES).hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.PUT, API_EPISODES).hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.DELETE, API_EPISODES).hasRole(ROLE_ADMIN)
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain mvcFilterChain(HttpSecurity http) {
        http
            .sessionManagement(session -> session
                    .enableSessionUrlRewriting(false)
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/auth/**", "/css/**", "/js/**", "/webjars/**",
                    "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                    "/h2-console/**"
                ).permitAll()
                .requestMatchers(
                    "/episodes/new", "/episodes/edit/**", "/episodes/delete/**"
                ).hasRole(ROLE_ADMIN)
                .requestMatchers("/episodes/**").hasAnyRole("USER", ROLE_ADMIN)
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/episodes", true)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .headers(headers -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                    XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                // CSP vulnerability fix
                .contentSecurityPolicy(csp -> csp
                        .policyDirectives("script-src 'self'; object-src 'none'; base-uri 'self'; form-action 'self'; frame-ancestors 'self'; default-src 'self'; style-src 'self';")
                )
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }
}

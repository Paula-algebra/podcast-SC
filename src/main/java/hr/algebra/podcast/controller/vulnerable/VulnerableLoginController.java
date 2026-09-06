package hr.algebra.podcast.controller.vulnerable;

import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;


@Controller
@Profile("sqli-demo")
@RequestMapping("/auth/demo")
public class VulnerableLoginController {

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/login")
    public String loginForm() {
        return "vulnerable/vulnerable_login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        // Dummy account, separate from the application's users.
        String sql = """
                SELECT username FROM (
                    SELECT 'admin' AS username, 'admin123' AS plain_password
                ) demo_users
                WHERE username = :username AND plain_password = :password
        """;

        var users = em.createNativeQuery(sql)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();

        if (users.isEmpty()) {
            return "Invalid credentials";
        }

        session.setAttribute("demoUser", users.getFirst());
        return "Demo login successful as: " + users.getFirst();
    }
}
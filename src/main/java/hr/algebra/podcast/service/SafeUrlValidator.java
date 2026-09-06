package hr.algebra.podcast.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * Centralised URL validator for the SECURE preview/import endpoints.
 *
 * <p>Defends against SSRF using a layered approach:
 * <ol>
 *   <li>Scheme allow-list — only {@code https} is accepted.</li>
 *   <li>Host allow-list — only known partner hosts are accepted.</li>
 *   <li>DNS resolution check — every resolved IP must be public, blocking
 *       loopback (127.x), site-local (10.x, 192.168.x), link-local
 *       (169.254.x — AWS IMDS) and any-local addresses.
 *       This stops DNS-rebinding attacks.</li>
 * </ol>
 */
@Service
public class SafeUrlValidator {

    private static final Logger log = LoggerFactory.getLogger(SafeUrlValidator.class);

    /**
     * Hosts the application is allowed to talk to.
     * In a real app this would come from configuration.
     */
    private static final Set<String> ALLOWED_HOSTS = Set.of(
            "raw.githubusercontent.com"
    );

    public URI validateOrThrow(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) {
            throw new IllegalArgumentException("URL must not be blank");
        }

        URI uri;
        try {
            uri = URI.create(rawUrl).normalize();
        } catch (IllegalArgumentException _) {
            throw new IllegalArgumentException("Malformed URL");
        }

        // 1. Scheme allow-list
        String scheme = uri.getScheme();
        if (scheme == null || !"https".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("Only https URLs are permitted");
        }

        // 2. Host allow-list
        String host = uri.getHost();
        if (host == null || !ALLOWED_HOSTS.contains(host.toLowerCase())) {
            throw new IllegalArgumentException("Host '" + host + "' is not on the allow-list");
        }

        // 3. DNS-resolution / IP check (blocks DNS-rebinding tricks)
        try {
            InetAddress[] addresses = InetAddress.getAllByName(host);
            for (InetAddress addr : addresses) {
                if (addr.isLoopbackAddress()
                        || addr.isSiteLocalAddress()
                        || addr.isLinkLocalAddress()
                        || addr.isAnyLocalAddress()
                        || addr.isMulticastAddress()) {
                    log.warn("Blocked SSRF attempt — host {} resolved to private IP {}",
                            host, addr.getHostAddress());
                    throw new IllegalArgumentException("Host resolves to a private network");
                }
            }
        } catch (UnknownHostException _) {
            throw new IllegalArgumentException("Could not resolve host");
        }

        return uri;
    }
}

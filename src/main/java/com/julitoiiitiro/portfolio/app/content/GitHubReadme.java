package com.julitoiiitiro.portfolio.app.content;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Fetches a project's README from GitHub and renders it to safe HTML for the project detail page,
 * so the page updates whenever the README on GitHub changes.
 * Results are cached for {@link #CACHE_TTL}; if GitHub can't be reached, the last good copy is used.
 */
@Component
public class GitHubReadme {

    static final Duration CACHE_TTL = Duration.ofMinutes(10);

    private static final Logger log = LoggerFactory.getLogger(GitHubReadme.class);

    /** https://github.com/{owner}/{repo}/blob/{branch}/{path} */
    private static final Pattern BLOB_URL =
            Pattern.compile("https://github\\.com/([\\w.-]+)/([\\w.-]+)/blob/([\\w./-]+?)/([\\w./-]+\\.md)");

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final Map<String, Cached> cache = new ConcurrentHashMap<>();

    public GitHubReadme() {
        List<org.commonmark.Extension> extensions = List.of(TablesExtension.create());
        this.parser = Parser.builder().extensions(extensions).build();
        this.renderer = HtmlRenderer.builder().extensions(extensions).build();
    }

    /** The README at {@code blobUrl} as sanitized HTML, or empty if it can't be fetched and nothing is cached. */
    public Optional<String> html(String blobUrl) {
        Optional<RepoFile> file = RepoFile.parse(blobUrl);
        if (file.isEmpty()) {
            log.warn("Ignoring readme '{}': expected https://github.com/<owner>/<repo>/blob/<branch>/<file>.md", blobUrl);
            return Optional.empty();
        }

        Cached cached = cache.get(blobUrl);
        if (cached != null && cached.fetchedAt().plus(CACHE_TTL).isAfter(Instant.now())) {
            return Optional.of(cached.html());
        }

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(file.get().rawUrl()))
                    .timeout(Duration.ofSeconds(4))
                    .build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalStateException("HTTP " + response.statusCode());
            }
            String html = render(response.body(), file.get());
            cache.put(blobUrl, new Cached(html, Instant.now()));
            return Optional.of(html);
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.warn("Could not fetch README {}: {}", blobUrl, e.getMessage());
            return Optional.ofNullable(cached).map(Cached::html);
        }
    }

    /** Markdown to sanitized HTML: relative links and images point at GitHub, the top title is dropped. */
    String render(String markdown, RepoFile file) {
        Document doc = Jsoup.parseBodyFragment(renderer.render(parser.parse(markdown)));

        // The page already shows the project name, so drop the README's leading title.
        Element first = doc.body().firstElementChild();
        if (first != null && first.tagName().equals("h1")) {
            first.remove();
        }

        for (Element img : doc.select("img[src]")) {
            img.attr("src", file.resolve(img.attr("src"), file.rawDir()));
        }
        for (Element link : doc.select("a[href]")) {
            link.attr("href", file.resolve(link.attr("href"), file.blobDir()));
        }

        String clean = Jsoup.clean(doc.body().html(), Safelist.relaxed().addProtocols("a", "href", "mailto"));

        Document safe = Jsoup.parseBodyFragment(clean);
        for (Element link : safe.select("a[href^=http]")) {
            link.attr("target", "_blank").attr("rel", "noopener noreferrer");
        }
        for (Element img : safe.select("img")) {
            img.attr("loading", "lazy");
        }
        return safe.body().html();
    }

    record RepoFile(String owner, String repo, String branch, String path) {

        static Optional<RepoFile> parse(String blobUrl) {
            if (blobUrl == null) {
                return Optional.empty();
            }
            Matcher m = BLOB_URL.matcher(blobUrl.trim());
            return m.matches()
                    ? Optional.of(new RepoFile(m.group(1), m.group(2), m.group(3), m.group(4)))
                    : Optional.empty();
        }

        String rawUrl() {
            return "https://raw.githubusercontent.com/%s/%s/%s/%s".formatted(owner, repo, branch, path);
        }

        String rawDir() {
            return dirOf("https://raw.githubusercontent.com/%s/%s/%s/".formatted(owner, repo, branch));
        }

        String blobDir() {
            return dirOf("https://github.com/%s/%s/blob/%s/".formatted(owner, repo, branch));
        }

        private String dirOf(String base) {
            int slash = path.lastIndexOf('/');
            return slash < 0 ? base : base + path.substring(0, slash + 1);
        }

        /** Absolute and anchor URLs are kept; relative ones are resolved against {@code dir}. */
        String resolve(String url, String dir) {
            try {
                if (url.isBlank() || url.startsWith("#") || URI.create(url.replace(" ", "%20")).isAbsolute()) {
                    return url;
                }
                return URI.create(dir).resolve(url.replace(" ", "%20")).toString();
            } catch (IllegalArgumentException e) {
                return url; // malformed URL: left as is, the sanitizer drops it
            }
        }
    }

    private record Cached(String html, Instant fetchedAt) {
    }
}

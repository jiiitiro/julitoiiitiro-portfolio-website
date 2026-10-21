package com.julitoiiitiro.portfolio.app.content;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * All site and resume content, bound from portfolio.yml.
 */
@ConfigurationProperties(prefix = "portfolio")
public record Portfolio(
        Profile profile,
        List<SkillGroup> skills,
        List<Project> projects,
        List<Experience> experience,
        List<Education> education,
        List<Certification> certifications) {

    public record Profile(
            String name,
            String title,
            String tagline,
            String summary,
            String shareDescription,
            String location,
            String email,
            String phone,
            String github,
            String siteUrl) {

        /** GitHub URL without the scheme, e.g. "github.com/jiiitiro". */
        public String githubLabel() {
            return github.replaceFirst("^https?://", "");
        }
    }

    public record SkillGroup(String name, String icon, String color, List<String> items) {
    }

    public record Project(
            String slug,
            String name,
            String period,
            String role,
            String color,
            List<String> highlights,
            String overview,
            List<String> features,
            List<String> techStack,
            List<String> challenges,
            List<String> lessons,
            List<Screenshot> screenshots,
            String github,
            String readme,
            String demo,
            String youtubeId,
            String sourceNote) {

        // Optional fields: missing lists become empty and blank strings become null,
        // so templates can simply test them with th:if.
        public Project {
            highlights = orEmpty(highlights);
            features = orEmpty(features);
            techStack = orEmpty(techStack);
            challenges = orEmpty(challenges);
            lessons = orEmpty(lessons);
            screenshots = orEmpty(screenshots);
            overview = blankToNull(overview);
            github = blankToNull(github);
            readme = blankToNull(readme);
            demo = blankToNull(demo);
            youtubeId = blankToNull(youtubeId);
            sourceNote = blankToNull(sourceNote);
        }

        /** Key features for the detail page, falling back to the card highlights until features are written. */
        public List<String> keyFeatures() {
            return features.isEmpty() ? highlights : features;
        }
    }

    public record Screenshot(String file, String caption) {
    }

    public record Experience(String title, String organization, String period, String color, String description) {
    }

    public record Education(String degree, String school, String period) {
    }

    public record Certification(String name, String provider, Status status, String color, String url) {

        public enum Status {
            COMPLETED, IN_PROGRESS
        }
    }

    public List<Certification> completedCertifications() {
        return certificationsWith(Certification.Status.COMPLETED);
    }

    public List<Certification> inProgressCertifications() {
        return certificationsWith(Certification.Status.IN_PROGRESS);
    }

    private List<Certification> certificationsWith(Certification.Status status) {
        return certifications.stream().filter(c -> c.status() == status).toList();
    }

    public Optional<Project> findProject(String slug) {
        return projects.stream().filter(p -> p.slug().equals(slug)).findFirst();
    }

    private static <T> List<T> orEmpty(List<T> list) {
        return list == null ? List.of() : list;
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}

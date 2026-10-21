package com.julitoiiitiro.portfolio.app.content;

import java.util.List;

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

    public record Project(String name, String period, String role, String color, List<String> highlights) {
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
}

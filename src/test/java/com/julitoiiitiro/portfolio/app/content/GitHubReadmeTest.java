package com.julitoiiitiro.portfolio.app.content;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GitHubReadmeTest {

	private final GitHubReadme readme = new GitHubReadme();

	private final GitHubReadme.RepoFile file =
			GitHubReadme.RepoFile.parse("https://github.com/jiiitiro/Demo/blob/main/README.md").orElseThrow();

	@Test
	void parsesGitHubBlobUrl() {
		assertThat(file.rawUrl()).isEqualTo("https://raw.githubusercontent.com/jiiitiro/Demo/main/README.md");
		assertThat(GitHubReadme.RepoFile.parse("https://example.com/README.md")).isEmpty();
		assertThat(GitHubReadme.RepoFile.parse("https://github.com/jiiitiro/Demo")).isEmpty();
	}

	@Test
	void dropsLeadingTitleAndKeepsContent() {
		String html = readme.render("# Demo App\n\nIntro text.\n\n## Features\n\n* One\n", file);

		assertThat(html).doesNotContain("Demo App").contains("Intro text.").contains("<h2>Features</h2>").contains("<li>One</li>");
	}

	@Test
	void resolvesRelativeLinksAndImagesToGitHub() {
		String html = readme.render("See [security](SECURITY.md) and ![shot](docs/shot.png)", file);

		assertThat(html)
				.contains("href=\"https://github.com/jiiitiro/Demo/blob/main/SECURITY.md\"")
				.contains("src=\"https://raw.githubusercontent.com/jiiitiro/Demo/main/docs/shot.png\"")
				.contains("target=\"_blank\"");
	}

	@Test
	void rendersTables() {
		String html = readme.render("| A | B |\n|---|---|\n| 1 | 2 |\n", file);

		assertThat(html).contains("<table>").contains("<td>1</td>");
	}

	@Test
	void stripsUnsafeHtml() {
		String html = readme.render("Hi <script>alert(1)</script> <a href=\"javascript:alert(1)\">x</a> <img src=x onerror=alert(1)>", file);

		assertThat(html).doesNotContain("<script").doesNotContain("javascript:").doesNotContain("onerror");
	}
}

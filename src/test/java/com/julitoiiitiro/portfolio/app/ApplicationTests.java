package com.julitoiiitiro.portfolio.app;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.julitoiiitiro.portfolio.app.content.Portfolio;
import com.julitoiiitiro.portfolio.app.resume.ResumePdfRenderer;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private Portfolio portfolio;

	@Autowired
	private ResumePdfRenderer resumePdfRenderer;

	@Test
	void contextLoads() {
	}

	@Test
	void portfolioContentIsLoadedFromYaml() {
		assertThat(portfolio.profile().name()).isNotBlank();
		assertThat(portfolio.skills()).isNotEmpty();
		assertThat(portfolio.projects()).isNotEmpty();
		assertThat(portfolio.experience()).isNotEmpty();
		assertThat(portfolio.education()).isNotEmpty();
		assertThat(portfolio.certifications()).allSatisfy(cert -> assertThat(cert.status()).isNotNull());
		assertThat(portfolio.completedCertifications().size() + portfolio.inProgressCertifications().size())
				.isEqualTo(portfolio.certifications().size());
	}

	@Test
	void resumeRendersToPdf() {
		byte[] pdf = resumePdfRenderer.render(portfolio);

		assertThat(new String(pdf, 0, 5, StandardCharsets.US_ASCII)).isEqualTo("%PDF-");
	}

}

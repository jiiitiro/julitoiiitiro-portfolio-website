package com.julitoiiitiro.portfolio.app.resume;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import com.julitoiiitiro.portfolio.app.content.Portfolio;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

/**
 * Renders the resume.html template to a PDF, so the PDF always matches the /resume page.
 */
@Component
public class ResumePdfRenderer {

    private final ITemplateEngine templateEngine;

    public ResumePdfRenderer(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] render(Portfolio portfolio) {
        String html = templateEngine.process("resume",
                new Context(Locale.ENGLISH, Map.of("portfolio", portfolio)));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            // OpenHTMLtoPDF needs well-formed XML; jsoup turns the HTML5 output into a W3C document.
            builder.withW3cDocument(new W3CDom().fromJsoup(Jsoup.parse(html)), "/");
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not render resume PDF", e);
        }
    }
}

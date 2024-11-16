package edu.cfd.e_learningPlatform.utils;

import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class PdfGenerator {

    @Autowired
    private TemplateEngine templateEngine;

    public byte[] generateCoursePdf(CourseResponse course) throws IOException {
        // Tạo đối tượng Thymeleaf Context
        Context context = new Context();
        // Truyền các biến vào Thymeleaf template
        context.setVariable("title", course.getTitle());
        context.setVariable("description", course.getDescription());
        context.setVariable("coverImage", course.getCoverImage());
        context.setVariable("price", course.getPrice());
        context.setVariable("level", course.getLevel());
        context.setVariable("instructor", course.getInstructor().getFullname());
        // Render HTML từ Thymeleaf template
        String htmlContent = templateEngine.process("course-template", context);
        // Tạo PDF từ HTML
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
}

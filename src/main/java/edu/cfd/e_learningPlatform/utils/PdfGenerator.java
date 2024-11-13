package edu.cfd.e_learningPlatform.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class PdfGenerator {

    // Phương thức nhận đối tượng CourseDto và tạo file PDF
    public static byte[] generateCoursePdf(CourseResponse course) throws IOException {
        // Tạo tài liệu PDF mới
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // Khởi tạo PdfWriter để ghi tài liệu vào ByteArrayOutputStream
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Thêm thông tin khóa học vào tài liệu PDF
            document.add(new Paragraph("Course Title: " + course.getTitle()));
            document.add(new Paragraph("Description: " + course.getDescription()));
            document.add(new Paragraph("Created At: " + course.getCreatedAt()));
            document.add(new Paragraph("Category: " + course.getCategory()));
            document.add(new Paragraph("Price: " + course.getPrice()));
            document.add(new Paragraph("Level: " + course.getLevel()));
            document.add(new Paragraph("Instructor: " + course.getInstructor().getFullname()));

            // Bạn có thể tiếp tục thêm thông tin bổ sung nếu cần (ví dụ: Sections, Cover Image, v.v.)

        } catch (DocumentException e) {
            // Nếu có lỗi trong quá trình tạo PDF, ném ra IOException
            throw new IOException("Error while generating PDF", e);
        } finally {
            // Đảm bảo đóng tài liệu PDF sau khi thêm thông tin
            document.close();
        }

        // Trả về file PDF dưới dạng byte array
        return byteArrayOutputStream.toByteArray();
    }
}

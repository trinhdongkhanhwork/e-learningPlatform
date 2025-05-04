package edu.cfd.e_learningPlatform.service.Impl;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import edu.cfd.e_learningPlatform.dto.LectureDto;
import edu.cfd.e_learningPlatform.dto.SectionDto;
import edu.cfd.e_learningPlatform.dto.request.ProgressUpdateRequest;
import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import edu.cfd.e_learningPlatform.dto.response.FirstIncompleteLectureResponse;
import edu.cfd.e_learningPlatform.entity.*;
import edu.cfd.e_learningPlatform.repository.*;
import edu.cfd.e_learningPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LearningProgressService {

    private final LearningProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final CertificateRepository certificateRepository;
    private final CourseServiceImpl courseService;
    private final CourseRepository courseRepository;
    private final UserService userService;
    private final EmailServiceImpl emailService;




    @Transactional
    public void updateProgress(ProgressUpdateRequest request) {
        User user = userService.getCurrentUser();
        Lecture lecture = lectureRepository.findById(request.getLectureId())
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        LearningProgress progress = progressRepository.findByUserAndLecture(user, lecture)
                .orElse(LearningProgress.builder()
                        .user(user)
                        .lecture(lecture)
                        .currentSecond(0)
                        .completed(false)
                        .build());

        if (request.getCurrentSecond() != null && request.getCurrentSecond() > progress.getCurrentSecond()) {
            progress.setCurrentSecond(request.getCurrentSecond());
        }

        if (request.getCurrentSecond() == null || progress.getCurrentSecond() == null) {
            progress.setCurrentSecond(0);
        }

        if (request.isCompleted()) {
            progress.setCompleted(true);
            if (progress.getCompletedAt() == null) {
                progress.setCompletedAt(LocalDateTime.now());
            }
        }

        progressRepository.save(progress);
    }


    public FirstIncompleteLectureResponse getFirstIncompleteLecture(Long idCourse) {
        User user = userService.getCurrentUser();

        CourseResponse course = courseService.getCourseById(idCourse);

        // Lấy tất cả LectureDto từ các Section
        List<LectureDto> allLectures = new ArrayList<>();
        for (SectionDto section : course.getSections()) {
            allLectures.addAll(section.getLectures());
        }

        int completedCount = 0;
        int total = allLectures.size();

        if (total == 0) {
            throw new RuntimeException("Lecture not found");
        }

        for (LectureDto lectureDto : allLectures) {
            Lecture lecture = lectureRepository.findById(lectureDto.getId())
                    .orElseThrow(() -> new RuntimeException("Lecture not found: " + lectureDto.getId()));

            Optional<LearningProgress> progressOpt = progressRepository.findByUserAndLecture(user, lecture);

            if (progressOpt.isPresent() && progressOpt.get().isCompleted()) {
                completedCount++;
            } else {
                int currentSecond = progressOpt.map(LearningProgress::getCurrentSecond).orElse(0);
                return new FirstIncompleteLectureResponse(
                        lecture.getId(),
                        currentSecond,
                        completedCount,
                        total,
                        false
                );
            }
        }

        // Nếu hoàn thành tất cả bài giảng, cấp chứng chỉ nếu chưa có, giửi mail
        Course entityCourse = courseRepository.findById(idCourse)
                .orElseThrow(() -> new RuntimeException("Course not found: " + idCourse));

        if (!certificateRepository.existsByUserAndCourse(user, entityCourse)) {
            Certificate cert = Certificate.builder()
                    .user(user)
                    .course(entityCourse)
                    .issuedAt(LocalDateTime.now())
                    .build();
            certificateRepository.save(cert);
            byte[] pdfBytes = generateCertificatePdf(user, entityCourse);
            emailService.sendCertificateEmail(user, entityCourse, pdfBytes);
        }

        return new FirstIncompleteLectureResponse(
                null,
                0,
                completedCount,
                total,
                true
        );
    }

    public byte[] generateCertificatePdf(User user, Course course) {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 14);

            document.add(new Paragraph("Certificate of Completion", titleFont));
            document.add(new Paragraph("\nThis is to certify that", normalFont));
            document.add(new Paragraph(user.getFullname(), titleFont));
            document.add(new Paragraph("has successfully completed the course:", normalFont));
            document.add(new Paragraph(course.getTitle(), titleFont));
            document.add(new Paragraph("\nDate: " + LocalDateTime.now().toLocalDate(), normalFont));

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error while generating PDF", e);
        }

        return baos.toByteArray();
    }

}

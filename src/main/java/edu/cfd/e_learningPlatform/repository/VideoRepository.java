package edu.cfd.e_learningPlatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.cfd.e_learningPlatform.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("SELECT v FROM Video v WHERE v.fileName = ?1")
    Video findByFileName(String fileName);

    Optional<Video> findByLectureId(Long lectureId);

    Video findByLecture_Id(long id);
}

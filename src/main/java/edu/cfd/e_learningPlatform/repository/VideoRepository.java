package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("SELECT v FROM Video v WHERE v.fileName = ?1")
    Video findByFileName(String fileName);
}

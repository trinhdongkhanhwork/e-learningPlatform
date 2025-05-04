package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {}

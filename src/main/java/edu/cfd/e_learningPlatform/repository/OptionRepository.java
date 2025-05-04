package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByIdIn(List<Long> optionIds);

}

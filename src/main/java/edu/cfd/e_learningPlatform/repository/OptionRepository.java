package edu.cfd.e_learningPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.cfd.e_learningPlatform.entity.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByIdIn(List<Long> optionIds);

}

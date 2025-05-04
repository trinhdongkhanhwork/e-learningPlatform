package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.SectionDto;

import java.util.List;

public interface SectionService {
    SectionDto createSection(SectionDto sectionDto);

    SectionDto getSectionById(Long id);

    List<SectionDto> getAllSections();

    SectionDto updateSection(Long id, SectionDto sectionDto);

    void deleteSection(Long id);
}

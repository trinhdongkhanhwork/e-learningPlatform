package edu.cfd.e_learningPlatform.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.dto.SectionDto;
import edu.cfd.e_learningPlatform.entity.Section;
import edu.cfd.e_learningPlatform.mapstruct.SectionMapper;
import edu.cfd.e_learningPlatform.repository.SectionRepository;
import edu.cfd.e_learningPlatform.service.SectionService;

@Service
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper = SectionMapper.INSTANCE;

    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public SectionDto createSection(SectionDto sectionDto) {
        Section section = sectionMapper.sectionDtoToSection(sectionDto);
        section = sectionRepository.save(section);
        return sectionMapper.sectionToSectionDto(section);
    }

    @Override
    public SectionDto getSectionById(Long id) {
        Section section = sectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        return sectionMapper.sectionToSectionDto(section);
    }

    @Override
    public List<SectionDto> getAllSections() {
        return sectionRepository.findAll().stream()
                .map(sectionMapper::sectionToSectionDto)
                .collect(Collectors.toList());
    }

    @Override
    public SectionDto updateSection(Long id, SectionDto sectionDto) {
        Section section = sectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        section.setTitle(sectionDto.getTitle());
        section = sectionRepository.save(section);
        return sectionMapper.sectionToSectionDto(section);
    }

    @Override
    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }
}

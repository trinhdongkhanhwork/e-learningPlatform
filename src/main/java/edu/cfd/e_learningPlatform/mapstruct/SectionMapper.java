package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.SectionDto;
import edu.cfd.e_learningPlatform.entity.Section;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SectionMapper {
    SectionMapper INSTANCE = Mappers.getMapper(SectionMapper.class);

    SectionDto sectionToSectionDto(Section section);

    Section sectionDtoToSection(SectionDto sectionDto);
}

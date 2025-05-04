package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.VideoDto;
import edu.cfd.e_learningPlatform.entity.Video;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VideoMapper {

    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);

    Video videoDtoToVideo(VideoDto videoDto);

    VideoDto videoToVideoDto(Video video);
}

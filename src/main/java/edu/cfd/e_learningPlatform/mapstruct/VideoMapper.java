package edu.cfd.e_learningPlatform.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import edu.cfd.e_learningPlatform.dto.VideoDto;
import edu.cfd.e_learningPlatform.entity.Video;

@Mapper
public interface VideoMapper {

    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);

    Video videoDtoToVideo(VideoDto videoDto);

    VideoDto videoToVideoDto(Video video);
}

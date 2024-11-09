package edu.cfd.e_learningPlatform.service;


import edu.cfd.e_learningPlatform.dto.VideoDto;

import java.util.List;

public interface VideoService {
    List<VideoDto> getAllVideos();
    VideoDto getVideoById(Long id);
    VideoDto createVideo(VideoDto videoDto);
    VideoDto updateVideo(Long id, VideoDto videoDto);
    void deleteVideo(Long id);
}

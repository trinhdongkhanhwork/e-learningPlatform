package edu.cfd.e_learningPlatform.service;

import java.util.List;

import edu.cfd.e_learningPlatform.dto.VideoDto;
import edu.cfd.e_learningPlatform.dto.response.VideoInlectureResponse;

public interface VideoService {
    VideoInlectureResponse getVideoLecture(Long videoId);

    List<VideoDto> getAllVideos();

    VideoDto getVideoById(Long id);

    VideoDto createVideo(VideoDto videoDto);

    VideoDto updateVideo(Long id, VideoDto videoDto);

    void deleteVideo(Long id);
}

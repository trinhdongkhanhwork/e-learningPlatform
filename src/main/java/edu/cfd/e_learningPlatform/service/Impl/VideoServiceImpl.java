package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.VideoDto;
import edu.cfd.e_learningPlatform.entity.Video;
import edu.cfd.e_learningPlatform.mapstruct.VideoMapper;
import edu.cfd.e_learningPlatform.repository.VideoRepository;
import edu.cfd.e_learningPlatform.service.VideoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper = VideoMapper.INSTANCE;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }


    @Override
    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(videoMapper::videoToVideoDto)
                .collect(Collectors.toList());
    }

    @Override
    public VideoDto getVideoById(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
        return videoMapper.videoToVideoDto(video);
    }

    @Override
    public VideoDto createVideo(VideoDto videoDto) {
        Video video = videoMapper.videoDtoToVideo(videoDto);
        video.setLecture(videoMapper.videoDtoToVideo(videoDto).getLecture());
        video = videoRepository.save(video);
        return videoMapper.videoToVideoDto(video);
    }

    @Override
    public VideoDto updateVideo(Long id, VideoDto videoDto) {
        Video video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
        video.setFileName(videoDto.getFileName());
        video.setDuration(videoDto.getDuration());
        video.setVideoUrl(videoDto.getVideoUrl());
        video = videoRepository.save(video);
        return videoMapper.videoToVideoDto(video);
    }

    @Override
    public void deleteVideo(Long id) {
        videoRepository.deleteById(id);
    }
}

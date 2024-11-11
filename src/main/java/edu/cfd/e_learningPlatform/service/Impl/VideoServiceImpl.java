package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.VideoDto;
import edu.cfd.e_learningPlatform.dto.response.CommentVideoResponse;
import edu.cfd.e_learningPlatform.dto.response.VideoInlectureResponse;
import edu.cfd.e_learningPlatform.entity.Video;
import edu.cfd.e_learningPlatform.mapstruct.VideoMapper;
import edu.cfd.e_learningPlatform.repository.CommentRepository;
import edu.cfd.e_learningPlatform.repository.VideoRepository;
import edu.cfd.e_learningPlatform.service.CommentService;
import edu.cfd.e_learningPlatform.service.VideoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final CommentService commentService;
    private final VideoMapper videoMapper = VideoMapper.INSTANCE;
    private final CommentRepository commentRepository;

    public VideoServiceImpl(VideoRepository videoRepository, CommentService commentService, CommentRepository commentRepository) {
        this.videoRepository = videoRepository;
        this.commentService = commentService;
        this.commentRepository = commentRepository;
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

    @Override
    public VideoInlectureResponse getVideoLecture(Long lectureId) {
        Video video = videoRepository.findByLecture_Id(lectureId);
        if (video == null) {
            return null;
        } else  {
            List<CommentVideoResponse> commentVideoResponse = commentService.getCommentVideo(video.getId());
            if(commentVideoResponse.size() == 0) {
                commentVideoResponse = null;
            }
            return new VideoInlectureResponse(
                    video.getId(),
                    video.getVideoUrl(),
                    commentVideoResponse
            );
        }
    }
}

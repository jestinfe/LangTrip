package kr.co.sist.e_learning.video;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VideoServiceImpl implements VideoService{
	
	@Autowired
	private VideoMapper videoMapper;
	
	@Override
	public void addVideo(VideoDTO cDTO) {
		videoMapper.insertVideo(cDTO);
	}
	
	@Override
	public List<VideoDTO> searchVideo() {
		
		return videoMapper.selectVideo();
	}
	
	@Override
	public VideoDTO showVideo(String videoSeq) {
		return videoMapper.showVideo(videoSeq);
	}

	@Override
	public List<VideoDTO> searchVideoByCourseSeq(String courseSeq) {
		return videoMapper.showVideoByCourseSeq(courseSeq);
	}

	@Override
	public int getMaxVideoOrder(String courseSeq) {
		Integer result = videoMapper.getMaxVideoOrder(courseSeq);
		int videoOrderValue = (result != null ? result : 0)+1;
		return videoOrderValue;
	}

	@Override
	public VideoDTO getVideoBySeq(int videoSeq) {
		VideoDTO vDTO = new VideoDTO();
		vDTO= videoMapper.getVideoBySeq(videoSeq);
		
		return vDTO;
	}

	@Override
	public void modifyVideo(VideoDTO vDTO) {
		videoMapper.modifyVideo(vDTO);
		
	}

	@Override
	public void deleteVideo(int videoSeq) {
		videoMapper.deleteVideo(videoSeq);
	}
	
	
	
	
}

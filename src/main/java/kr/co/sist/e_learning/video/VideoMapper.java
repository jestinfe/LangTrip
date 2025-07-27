package kr.co.sist.e_learning.video;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface VideoMapper {
	
	void insertVideo(VideoDTO cDTO);
	
	public List<VideoDTO> selectVideo();
	
	public VideoDTO showVideo(String videoSeq);
	
	public List<VideoDTO>showVideoByCourseSeq(String courseSeq);
	
	public Integer getMaxVideoOrder(String courseSeq);
	
	public VideoDTO getVideoBySeq(int videoSeq);
	
	public void modifyVideo(VideoDTO vDTO);
	
	public void deleteVideo(int videoSeq);
}

package kr.co.sist.e_learning.video;

import java.util.List;


public interface VideoService {
	
	void addVideo(VideoDTO cDTO);
	public List<VideoDTO> searchVideo();
	public VideoDTO showVideo(String videoSeq);
	public List<VideoDTO> searchVideoByCourseSeq(String courseSeq);
	
	public int getMaxVideoOrder(String courseSeq);
	
public VideoDTO getVideoBySeq(int videoSeq);
	
	public void modifyVideo(VideoDTO vDTO);

	public void deleteVideo(int videoSeq);
}

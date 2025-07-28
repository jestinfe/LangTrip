package kr.co.sist.e_learning.course;

public interface NoticeService {
	
void saveNotice(CourseNoticeDTO cnDTO);
	
	public CourseNoticeDTO getLatestNotice(String courseSeq);
	
	void saveLearning(CourseNoticeDTO cnDTO);

	public CourseNoticeDTO getLatestLearning(String courseSeq);
}

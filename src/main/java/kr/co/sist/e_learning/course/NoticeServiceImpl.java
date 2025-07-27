package kr.co.sist.e_learning.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService{

	@Autowired
	private NoticeMapper nm;
	@Override
	public void saveNotice(CourseNoticeDTO cnDTO) {
		nm.saveNotice(cnDTO);
	}

	@Override
	public CourseNoticeDTO getLatestNotice(String courseSeq) {
		
		return nm.getLatestNotice(courseSeq);
	}

	@Override
	public void saveLearning(CourseNoticeDTO cnDTO) {
		nm.saveLearning(cnDTO);
	}

	@Override
	public CourseNoticeDTO getLatestLearning(String courseSeq) {
		return nm.getLatestLearning(courseSeq);
	}

	
}

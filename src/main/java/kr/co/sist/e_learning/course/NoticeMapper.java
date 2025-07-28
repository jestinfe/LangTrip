package kr.co.sist.e_learning.course;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {
	void saveNotice(CourseNoticeDTO cnDTO);

	public CourseNoticeDTO getLatestNotice(String courseSeq);

	void saveLearning(CourseNoticeDTO cnDTO);

	public CourseNoticeDTO getLatestLearning(String courseSeq);

}

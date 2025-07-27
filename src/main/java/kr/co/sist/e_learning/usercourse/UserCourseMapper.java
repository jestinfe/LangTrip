package kr.co.sist.e_learning.usercourse;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserCourseMapper {

	public int insertUserCourse(UserCourseDTO ucDTO); // 사용자가 수강 눌렀을떄 사용자 수강 테이블에 추가
	public int selectAlreadyEnrollCourse(Map<String, Object> paramMap);
	public List<UserCourseDTO> selectUserCoursesByUserSeq(Long userId);

	public List<UserCourseDTO> selectUserCourseByPage(Map<String, Object> paramMap);

	public int selectUserCourseCount(Long userId);

    List<UserCourseListDisplayDTO> selectPublicCourses(Map<String, Object> params);
    int countPublicCourses(Map<String, Object> params);
    List<UserCourseListDisplayDTO> selectNewCourses(@Param("limit") int limit);
}

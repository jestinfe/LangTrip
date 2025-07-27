package kr.co.sist.e_learning.usercourse;

import java.util.List;
import java.util.Map;

import kr.co.sist.e_learning.pagination.PageResponseDTO;
import org.springframework.stereotype.Service;


public interface UserCourseService {
	
	public int addUserCourse(UserCourseDTO ucDTO);
	public List<UserCourseDTO> searchUserCourseByUserId(Long userId);
	public List<UserCourseDTO> searchUserCourseByPage(Map<String, Object> param);
	public int searchUserCourseCount(Long userId);
	public int selectAlreadyEnrollCourse(Map<String, Object> paramMap);
    PageResponseDTO<UserCourseListDisplayDTO> getPublicCourses(Map<String, Object> params);
    List<UserCourseListDisplayDTO> getNewCourses(int limit);
    List<UserCourseDTO> getRecentEnrolledCourses(long userSeq, int limit);
    int deleteUserCourse(UserCourseDTO dto);
}

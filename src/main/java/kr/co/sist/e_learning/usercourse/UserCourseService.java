package kr.co.sist.e_learning.usercourse;

import java.util.List;
import java.util.Map;

import kr.co.sist.e_learning.pagination.PageResponseDTO;
import org.springframework.stereotype.Service;


public interface UserCourseService {
	
	public void addUserCourse(UserCourseDTO ucDTO);
	public List<UserCourseDTO> searchUserCourseByUserId(String userId);
	public List<UserCourseDTO> searchUserCourseByPage(Map<String, Object> param);
	public int searchUserCourseCount(String userId);

    PageResponseDTO<UserCourseListDisplayDTO> getPublicCourses(Map<String, Object> params);
    List<UserCourseListDisplayDTO> getNewCourses(int limit);
}

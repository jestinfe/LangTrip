package kr.co.sist.e_learning.usercourse;

import java.util.List;
import java.util.Map;

import kr.co.sist.e_learning.pagination.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserCourseServiceImpl implements UserCourseService{

	@Autowired
	private UserCourseMapper ucm;

	@Override
	public int addUserCourse(UserCourseDTO ucDTO) {
		int result = ucm.insertUserCourse(ucDTO);
		return result;
	}

	@Override
	public List<UserCourseDTO> searchUserCourseByUserId(Long userId) {
		List<UserCourseDTO> list =  ucm.selectUserCoursesByUserSeq(userId);
		return list;
	}

	@Override
	public List<UserCourseDTO> searchUserCourseByPage(Map<String, Object> param) {
		
		return ucm.selectUserCourseByPage(param);
	}

	@Override
	public int searchUserCourseCount(Long userId) {
		int userCourseCount=ucm.selectUserCourseCount(userId);
		return userCourseCount;
	}

    @Override
    public PageResponseDTO<UserCourseListDisplayDTO> getPublicCourses(Map<String, Object> params) {
        int page = (int) params.get("page");
        int pageSize = (int) params.get("pageSize");

        List<UserCourseListDisplayDTO> list = ucm.selectPublicCourses(params);
        int totalCount = ucm.countPublicCourses(params);

        return new PageResponseDTO<>(list, totalCount, page, pageSize, 5);
    }

    @Override
    public List<UserCourseListDisplayDTO> getNewCourses(int limit) {
        return ucm.selectNewCourses(limit);
    }

}

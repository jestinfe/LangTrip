package kr.co.sist.e_learning.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.sist.e_learning.pagination.UsrAndRptPageRequestDTO;
import kr.co.sist.e_learning.pagination.UsrAndRptPageResponseDTO;

@Service
public class AdminUserService {
	private final AdminUserMapper adminUserMapper;
	
	// 생성자 주입
	@Autowired
	public AdminUserService(AdminUserMapper adminUserMapper) {
		this.adminUserMapper = adminUserMapper;
	}
	
	// 회원 목록
	public UsrAndRptPageResponseDTO<UserDTO> getUserList(UsrAndRptPageRequestDTO reqDTO) {
		List<UserDTO> list = adminUserMapper.selectUserList(reqDTO);
		int totalCnt = adminUserMapper.countUserList(reqDTO);
		
		
		return new UsrAndRptPageResponseDTO<UserDTO>(list, totalCnt, reqDTO.getPage());
	}
	
	// 회원 상세 조회(모달용)
	public UserDTO getUserDetail(long userSeq) {
		UserDTO user = adminUserMapper.selectUserDetail(userSeq);

		// 수강 중 과목
		List<String> activeCourses = adminUserMapper.selectActiveCourses(userSeq);
		user.setCourseTitles(activeCourses);
		
		// 개설한 과목
		List<String> openedCourses = adminUserMapper.selectOpenedCourses(userSeq);
		user.setOpenedCourses(openedCourses);
		
		return user;
	}
	
	@Transactional
	public void updateUserStatus(UserDTO dto) {
		Long reportId = adminUserMapper.selectLatestReportId(dto.getUserSeq());
		if (reportId != null) {
			dto.setReportId(reportId);
			
			adminUserMapper.updateReport(dto);
		}
			
		// 신고 이력 없는 회원은 users 테이블만 update
		adminUserMapper.updateUserStatus(dto);
	}
}

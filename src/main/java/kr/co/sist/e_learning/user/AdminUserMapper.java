package kr.co.sist.e_learning.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.e_learning.pagination.UsrAndRptPageRequestDTO;

@Mapper
public interface AdminUserMapper {
	public List<UserDTO> selectUserList(UsrAndRptPageRequestDTO dto);
	
	public int countUserList(UsrAndRptPageRequestDTO dto);
	
	public UserDTO selectUserDetail(@Param("userSeq") long userSeq);
	
	public List<String> selectActiveCourses(@Param("userSeq") long userSeq);
	
	public List<String> selectOpenedCourses(@Param("userSeq") long userSeq);
	
	public Long selectLatestReportId(Long userSeq);
	
	public void updateReport(UserDTO dto);
	
	public void deleteReasonByReportId(UserDTO dto);
	
	public void insertReasons(UserDTO dto);
	
	public void updateUserStatus(UserDTO dto);
}

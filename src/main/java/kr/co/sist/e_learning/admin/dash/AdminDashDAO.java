package kr.co.sist.e_learning.admin.dash;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.e_learning.admin.dash.AdminDashDTO;

@Mapper
public interface AdminDashDAO {
	List<AdminDashDTO> getHourlyAccessStats();
    List<AdminDashDTO> getDailySignupStats();
    List<AdminDashDTO> getSignupPathStats();
    List<AdminDashDTO> getUnsignReasonStats();
    List<AdminDashDTO> getCourseCategoryStats();
    List<AdminDashDTO> getCourseDifficultyStats(); 
    List<AdminDashDTO> getAdClickStats();
}

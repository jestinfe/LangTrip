package kr.co.sist.e_learning.admin.dash;

import java.util.List;

import kr.co.sist.e_learning.admin.log.AdminLogDTO;

public interface AdminDashService {
	List<AdminDashDTO> getHourlyAccessStats();
    List<AdminDashDTO> getDailySignupStats();
    List<AdminDashDTO> getSignupPathStats();
    List<AdminDashDTO> getUnsignReasonStats();
    List<AdminDashDTO> getCourseCategoryStats();
    List<AdminDashDTO> getCourseDifficultyStats(); 
    List<AdminDashDTO> getAdClickStats();
}

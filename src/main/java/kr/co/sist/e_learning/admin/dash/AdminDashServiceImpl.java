package kr.co.sist.e_learning.admin.dash;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("admin/dashServiceImpl")
public class AdminDashServiceImpl implements AdminDashService {
    @Autowired
    private AdminDashDAO adDAO;
    
    @Override
    public List<AdminDashDTO> getHourlyAccessStats() {
    	return adDAO.getHourlyAccessStats();
    }
    
    @Override
    public List<AdminDashDTO> getDailySignupStats() {
        List<AdminDashDTO> signList = adDAO.getDailySignupStats();
        return signList;
    }
    
    @Override
    public List<AdminDashDTO> getSignupPathStats() {
        List<AdminDashDTO> pathList = adDAO.getSignupPathStats();
        return pathList;
    }

    @Override
    public List<AdminDashDTO> getUnsignReasonStats() {
        List<AdminDashDTO> reasonList = adDAO.getUnsignReasonStats();
        return reasonList;
    }
    
    @Override
    public List<AdminDashDTO> getCourseCategoryStats() {
        return adDAO.getCourseCategoryStats();
    }
    
    @Override
    public List<AdminDashDTO> getCourseDifficultyStats() {
        return adDAO.getCourseDifficultyStats();
    }
    
    @Override
    public List<AdminDashDTO> getAdClickStats() {
        List<AdminDashDTO> accessList = adDAO.getAdClickStats();
        System.out.println("시간별 접속 통계: " + accessList);  // 결과 출력
        return accessList;
    }
}

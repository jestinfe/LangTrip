package kr.co.sist.e_learning.admin.dash;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.e_learning.admin.log.AdminLogService;

@Controller
public class AdminDashController {

    @Autowired
    private AdminLogService adminLogService;
    
    @Autowired
    @Qualifier("admin/dashServiceImpl")
    private AdminDashService adSV;

    // 페이지 진입용
    @GetMapping("/admin/dash/user_statistics")
    public String userStatisticsPage() {
        return "admin/dash/user_statistics"; 
    }

    // AJAX 데이터 요청
    @ResponseBody
    @GetMapping("/admin/api/user_stats")
    public Map<String, Object> getUserStats() {
    	Map<String, Object> stats = new HashMap<>();
    	stats.put("hourlyStats", adSV.getHourlyAccessStats());
        stats.put("dailySignup", adSV.getDailySignupStats());
        stats.put("signupPath", adSV.getSignupPathStats());
        stats.put("unsignReason", adSV.getUnsignReasonStats());
        stats.put("courseCategoryStats", adSV.getCourseCategoryStats());
        stats.put("courseDifficultyStats", adSV.getCourseDifficultyStats());
        stats.put("adClickStats", adSV.getAdClickStats()); 
        return stats;
    	}
    
}

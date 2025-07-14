package kr.co.sist.e_learning.admin.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class AdminLogController {

    @Autowired
    private AdminLogService adminLogService;

    @GetMapping("/admin/log")
    public String showLogPage(Model model) {
        List<AdminLogDTO> logs = adminLogService.getAllLogs();
        model.addAttribute("logs", logs);
        return "admin/log/admin_log";
    }
}

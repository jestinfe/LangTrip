package kr.co.sist.e_learning.admin.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;

@Controller
public class AdminLogController {

    @Autowired
    private AdminLogService adminLogService;

    @GetMapping("/admin/log")
    public String adminLogPage(PageRequestDTO pageRequestDTO, AdminLogDTO searchDTO, Model model) {
        PageResponseDTO<AdminLogDTO> pageResponse = adminLogService.getAdminLogs(pageRequestDTO, searchDTO);
        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("searchDTO", searchDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        return "admin/log/admin_log";
    }
}
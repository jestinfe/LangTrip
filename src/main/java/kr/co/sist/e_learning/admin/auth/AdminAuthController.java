package kr.co.sist.e_learning.admin.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private AdminAuthService adminAuthService;

    @GetMapping("/login")
    public String loginForm() {
        return "admin/admin_login";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String adminId,
                               @RequestParam String adminPw,
                               HttpSession session,
                               Model model) {
        AdminAuthDTO admin = adminAuthService.login(adminId, adminPw);

        if (admin != null) {
            session.setAttribute("admin", admin);
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            return "admin/admin_login";
        }
    }
}


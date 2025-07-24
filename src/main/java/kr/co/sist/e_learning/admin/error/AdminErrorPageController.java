package kr.co.sist.e_learning.admin.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminErrorPageController {

    @GetMapping("/admin/access-denied")
    public String accessDenied() {
        return "admin/error/access_denied";
    }
}

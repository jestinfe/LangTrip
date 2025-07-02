package kr.co.sist.e_learning.admin.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminSignupController {

    @Autowired
    @Qualifier("adminSignupServiceImpl")
    private AdminSignupService signupService;

    @GetMapping("/signup")
    public String signupForm() {
        return "admin/signup";
    }

    @PostMapping("/signup")
    public String signupProcess(@ModelAttribute AdminSignupDTO dto, Model model) {
        boolean result = signupService.registerAdmin(dto);
        if (result) {
            return "redirect:/admin/login";
        } else {
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "admin/signup";
        }
    }
    
    @PostMapping("/send-code")
    @ResponseBody
    public ResponseEntity<String> sendCode(@RequestParam String email) {
    	signupService.sendVerificationCode(email);
        return ResponseEntity.ok("인증코드 전송됨");
    }

    @PostMapping("/verify-code")
    @ResponseBody
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean result = signupService.verifyEmailCode(email, code);
        return result ? ResponseEntity.ok("success") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
    }


}


package kr.co.sist.e_learning.admin.signup;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdminSignupController {

    @Autowired
    private AdminSignupService signupService;

    // 이메일 인증 코드 발송
    @PostMapping("/send-code")
    @ResponseBody
    public void sendVerification(@RequestParam String email) {
        signupService.sendVerificationCode(email);
    }

    // 인증코드 확인
    @PostMapping("/verify-code")
    @ResponseBody
    public String verify(@RequestParam String email, @RequestParam String code) {
        boolean result = signupService.verifyCode(email, code);
        return result ? "success" : "fail";
    }

    // 회원가입 요청 저장
    @PostMapping("/signup")
    @ResponseBody
    public void signupProcess(@RequestBody AdminSignupDTO dto) {
        dto.setRequestId(UUID.randomUUID().toString()); // 트리거가 없어도 유니크하게 생성
        signupService.registerAdmin(dto);
    }
    
    // 회원가입 요청 저장
    @GetMapping("/signup")
    public String signup() {
    	return "admin/signup/signup";
    }
}

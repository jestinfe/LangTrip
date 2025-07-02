package kr.co.sist.e_learning.admin.auth;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private AdminAuthService adminAuthService;

    private static final String SECRET_KEY = "6LeYf3IrAAAAABPv4BNZ64w7TDtHeFuHXvlG1mRQ"; // 🔐 여기에 구글에서 받은 시크릿 키 삽입

    @GetMapping("/login")
    public String loginForm() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String adminId,
                               @RequestParam String adminPw,
                               @RequestParam(name = "g-recaptcha-response") String recaptchaResponse,
                               HttpSession session,
                               Model model) {

        if (!verifyRecaptcha(recaptchaResponse)) {
            model.addAttribute("error", "자동 로그인 방지를 확인해주세요.");
            return "admin/login";
        }

        AdminAuthDTO admin = adminAuthService.login(adminId, adminPw);

        if (admin != null) {
            session.setAttribute("admin", admin);
            return "admin/dashboard";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            return "admin/login";
        }
    }
    

    

    // ✅ reCAPTCHA 검증 메서드
    private boolean verifyRecaptcha(String recaptchaResponse) {
        try {
            String apiUrl = "https://www.google.com/recaptcha/api/siteverify";
            String body = "secret=" + SECRET_KEY + "&response=" + recaptchaResponse;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "?" + body))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.body());

            return jsonNode.get("success").asBoolean();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

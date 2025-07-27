package kr.co.sist.e_learning.main;

import kr.co.sist.e_learning.usercourse.UserCourseDTO;
import kr.co.sist.e_learning.usercourse.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private UserCourseService userCourseService;

    private long getOrInitUserSeq(Authentication auth) {
        Object raw = auth.getPrincipal();
        if (raw instanceof Long userSeq) {
            return userSeq;
        }
        return 1001L; // Default or error value
    }

	@GetMapping("/")
	public String main(Model model, Authentication authentication) {
        boolean loggedIn = authentication != null && authentication.isAuthenticated() && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken);
		model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("newCourses", userCourseService.getNewCourses(6)); // Fetch 5 new courses
		return "main";
	}

    @GetMapping("/api/main/enrolledCourses")
    @ResponseBody
    public ResponseEntity<List<UserCourseDTO>> getEnrolledCourses(Authentication authentication) {
        boolean loggedIn = authentication != null && authentication.isAuthenticated() && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken);

        if (loggedIn) {
            long userSeq = getOrInitUserSeq(authentication);
            Map<String, Object> param = new HashMap<>();
            param.put("userSeq", userSeq);
            param.put("offset", 0);
            param.put("limit", 3);
            List<UserCourseDTO> enrolledCourses = userCourseService.searchUserCourseByPage(param);
            return ResponseEntity.ok(enrolledCourses);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }
}



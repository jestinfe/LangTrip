package kr.co.sist.e_learning.main;

import kr.co.sist.e_learning.usercourse.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private UserCourseService userCourseService;

	@GetMapping("/")
	public String main(Model model) {
		model.addAttribute("loggedIn", false); // or true if authenticated
        model.addAttribute("newCourses", userCourseService.getNewCourses(5)); // Fetch 5 new courses
		return "main";
	}
	
}


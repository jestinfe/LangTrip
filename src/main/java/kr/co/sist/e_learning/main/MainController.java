package kr.co.sist.e_learning.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
//ㅇㅇ
	@GetMapping("/")
	public String main(Model model) {
		model.addAttribute("loggedIn", false); // or true if authenticated
		return "ksh/main";
	}
}

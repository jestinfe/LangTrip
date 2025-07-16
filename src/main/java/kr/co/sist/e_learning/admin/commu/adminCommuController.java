package kr.co.sist.e_learning.admin.commu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class adminCommuController {

	
	 @GetMapping("/commu")
	 public String commulist() {
		 return "/commu/commu";
	 }
}

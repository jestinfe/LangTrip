package kr.co.sist.e_learning.mypage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

 @GetMapping("/user_header")
 public String userHeader() {
 return "mypage/user_header";
 }
	 
	 
 @GetMapping("/sidebar")
 public String sidebarPage() {
     return "mypage/sidebar";
 }
 
 
 @GetMapping("/donation_history")
 public String donation_history() {
	 return "mypage/donation_history";
 }
 
 
 @GetMapping("/lecture_history")
 public String lecture_history() {
	 return "mypage/lecture_history";
 }
 
 
 @GetMapping("/leave")
 public String leave() {
	 return "mypage/leave";
 }
 
 @GetMapping("/my_info")
 public String myInfo() {
     return "mypage/my_info";
 }

 @GetMapping("/reset_password")
 public String resetPassword() {
     return "mypage/reset_password";
 }

 @GetMapping("/link_account")
 public String linkAccount() {
     return "mypage/link_account";
 }


 
}//class

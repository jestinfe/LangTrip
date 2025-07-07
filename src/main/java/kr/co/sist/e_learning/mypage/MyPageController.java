package kr.co.sist.e_learning.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

@Autowired
private MyPageService mps;

private String getOrInitUserSeq(HttpSession session) {
    String userSeq = (String) session.getAttribute("user_seq");
    if (userSeq == null) {
        userSeq = "user001"; // 테스트용
        session.setAttribute("user_seq", userSeq);
    }
    return userSeq;
}
//	
//@GetMapping({ "", "/" })
//public String myPageMain(Model model) {
//    // 로그인된 유저 ID가 없으면 guest로 대체
//    String userId = "testUser"; // 또는 세션에서 꺼냄
//    MyPageDTO myData = null;
//
//    try {
//        myData = mps.getMyPageData(userId);
//    } catch (Exception e) {
//        myData = new MyPageDTO(); // GUEST용 기본 객체 생성
//        myData.setUserId("GUEST");
//        myData.setName("비회원");
//        // 나머지 필드는 0 또는 기본값 처리
//    }
//
//    model.addAttribute("myData", myData);
//    return "mypage/mypage_main";
//}

@GetMapping
public String mypageMain() {
    return "mypage/mypage_main"; // templates/mypage/mypage_main.html
}

//대시 보드
@GetMapping("/dashboard")
public String dashboard(HttpSession session, Model model) {
	 String userSeq = getOrInitUserSeq(session);

    MyPageDTO myData = mps.getMyPageData(userSeq);
    model.addAttribute("myData", myData);

    return "mypage/dashboard";
}

// 수강 내역
@GetMapping("/lecture_history")
public String lectureHistory(HttpSession session, Model model) {
	String userSeq = getOrInitUserSeq(session);
    
    List<LectureHistoryDTO> list = mps.getLectureHistory(userSeq);
    model.addAttribute("lectureList", list);
    
    return "mypage/lecture_history";
}

	
 @GetMapping("/user_header")
 public String userHeader() {
 return "mypage/user_header";
 }
	 
	 
 @GetMapping("/sidebar")
 public String sidebarPage() {
     return "mypage/sidebar";
 }

 
 @GetMapping("/subscriptions")
 public String subscriptions(HttpSession session, Model model) {
	 String userSeq = getOrInitUserSeq(session);

     List<SubscriptionDTO> list = mps.getSubscriptions(userSeq);
     model.addAttribute("subscriptionList", list);

     return "mypage/subscriptions";
 }

 @PostMapping("/unsubscribe")
 public String unsubscribe(@RequestParam String instructorId) {
     String userId = "testUser"; // 나중에 세션에서 가져오기
     mps.cancelSubscription(userId, instructorId);
     return "mypage/subscriptions";
 }
 
 @DeleteMapping("/cancel_subscription")
 @ResponseBody
 public ResponseEntity<Void> cancelSubscription(@RequestParam("instructorId") String instructorId,
                                                HttpSession session) {
	 String userSeq = getOrInitUserSeq(session);
     if (userSeq == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
     }

     mps.cancelSubscription(userSeq, instructorId);
     return ResponseEntity.ok().build();
 }
 
 @GetMapping("/donation_history")
 public String donation_history() {
	 return "mypage/donation_history";
 }
 
 @GetMapping("/leave")
 public String leave() {
	 return "mypage/leave";
 }
 
 @GetMapping("/my_info")
 public String myInfo(HttpSession session, Model model) {
     String userSeq = getOrInitUserSeq(session);

     MyPageDTO myData = mps.getUserInfo(userSeq);  // ✅ 정확한 메서드로 교체
     model.addAttribute("myData", myData);

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

package kr.co.sist.e_learning.mypage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private MyPageService mps;

    private long getOrInitUserSeq(HttpSession session) {
        Object raw = session.getAttribute("user_seq");
        long userSeq;
        if (raw == null) {
            userSeq = 1001L; // 테스트용
            session.setAttribute("user_seq", userSeq);
        } else {
            userSeq = (raw instanceof Long) ? (Long) raw : Long.parseLong(raw.toString());
        }
        return userSeq;
    }



    /** 공통 진입점 (탭 파라미터 기반) */
    @GetMapping
    public String mypageMain(@RequestParam(value = "tab", required = false, defaultValue = "dashboard") String tab,
                             HttpSession session, Model model) {
    	long userSeq = getOrInitUserSeq(session);

        switch (tab) {
            case "my_info":
                model.addAttribute("myData", mps.getUserInfo(userSeq));
                break;
            case "lecture_history":
                model.addAttribute("lectureList", mps.getLectureHistory(userSeq));
                break;
            case "subscriptions":
                model.addAttribute("subscriptionList", mps.getSubscriptions(userSeq));
                break;
            case "dashboard":
            default:
                model.addAttribute("myData", mps.getMyPageData(userSeq));
                break;
        }

        model.addAttribute("currentTab", tab);
        return "mypage/mypage_main";
    }
    

	/** 구독 목록 */
    @GetMapping("/subscriptions")
    public String subscriptions(@SessionAttribute("user_seq") long userSeq, Model model) {
        List<SubscriptionDTO> subs = mps.getSubscriptions(userSeq);
        System.out.println("구독 목록 사이즈: " + subs.size());
        for (SubscriptionDTO dto : subs) {
            System.out.println(dto); // 혹은 dto.getNickname(), dto.getThumbnailPath() 등
        }
        model.addAttribute("subscriptions", subs);
        return "mypage/subscriptions";
    }

    /** 구독 취소 (폼 전송용) */
    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestParam String instructorId, HttpSession session) {
    	long userSeq = getOrInitUserSeq(session);
        mps.cancelSubscription(userSeq, instructorId);
        return "redirect:/mypage?tab=subscriptions";
    }

    /** 구독 취소 (AJAX용) */
    @DeleteMapping("/cancel_subscription")
    @ResponseBody
    public ResponseEntity<Void> cancelSubscription(@RequestParam("instructorId") String instructorId,
                                                   HttpSession session) {
        long userSeq = getOrInitUserSeq(session);
        mps.cancelSubscription(userSeq, instructorId);
        return ResponseEntity.ok().build();
    }
    

    /** 대시보드 단독 접근용 (직접 경로 접근시) */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
    	long userSeq = getOrInitUserSeq(session);
        model.addAttribute("myData", mps.getMyPageData(userSeq));
        return "mypage/dashboard";
    }

    /** 수강 내역 */
    @GetMapping("/lecture_history")
    public String lectureHistory(HttpSession session, Model model) {
    	long userSeq = getOrInitUserSeq(session);
        model.addAttribute("lectureList", mps.getLectureHistory(userSeq));
        return "mypage/lecture_history";
    }

    
    /** 내 정보 */
    @GetMapping("/my_info")
    public String myInfo(HttpSession session, Model model) {
    	long userSeq = getOrInitUserSeq(session);
        System.out.println("uploadProfile 메서드 진입");
        model.addAttribute("myData", mps.getUserInfo(userSeq));
        return "mypage/my_info";
    }

    /** 프로필 업로드 */
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload_profile")
    @ResponseBody
    public Map<String, Object> uploadProfile(@RequestParam("file") MultipartFile file,
                                             HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        Object obj = session.getAttribute("user_seq");
        if (!(obj instanceof Long userSeq) || file.isEmpty()) {
            result.put("success", false);
            result.put("message", "사용자 정보 또는 파일이 없습니다.");
            return result;
        }

        try {
            // 확장자 검사
            String originalName = file.getOriginalFilename();
            String ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
            if (!List.of("png", "jpg", "jpeg", "gif").contains(ext)) {
                result.put("success", false);
                result.put("message", "허용되지 않은 확장자입니다.");
                return result;
            }

            // 저장 디렉토리 생성
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // 기존 이미지 삭제
            String oldPath = mps.selectProfilePath(userSeq); // 예: /images/userprofile/abc_user001.png
            if (oldPath != null && oldPath.startsWith("/images/userprofile/")) {
                String oldFileName = oldPath.replace("/images/userprofile/", "");
                File oldFile = new File(dir, oldFileName);
                if (oldFile.exists()) {
                    oldFile.delete();
                    System.out.println("기존 파일 삭제됨: " + oldFile.getAbsolutePath());
                }
            }

            // 새 파일명 생성
            String uuid = UUID.randomUUID().toString();
            String newFileName = uuid + "_" + userSeq + "." + ext;
            File destFile = new File(dir, newFileName);
            file.transferTo(destFile);

            // DB 업데이트
            String webPath = "/images/userprofile/" + newFileName;
            mps.updateProfilePath(userSeq, webPath);

            result.put("success", true);
            result.put("newPath", webPath);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }


    /** 비밀번호 변경 */
    @GetMapping("/reset_password")
    public String resetPassword() {
        return "mypage/reset_password";
    }

    /** 계좌 연동 */
    @GetMapping("/link_account")
    public String linkAccount() {
        return "mypage/link_account";
    }

    /** 탈퇴 페이지 */
    @GetMapping("/leave")
    public String leave() {
        return "mypage/leave";
    }

    /** 후원 기록 */
    @GetMapping("/donation_history")
    public String donation_history() {
        return "mypage/donation_history";
    }

    /** 헤더/사이드바 (프래그먼트) */
    @GetMapping("/user_header")
    public String userHeader() {
        return "mypage/user_header";
    }

    @GetMapping("/sidebar")
    public String sidebarPage() {
        return "mypage/sidebar";
    }

}
